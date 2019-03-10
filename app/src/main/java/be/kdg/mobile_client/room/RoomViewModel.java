package be.kdg.mobile_client.room;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.room.model.Act;
import be.kdg.mobile_client.room.model.ActType;
import be.kdg.mobile_client.room.model.Player;
import be.kdg.mobile_client.round.Round;
import be.kdg.mobile_client.round.RoundRepository;
import be.kdg.mobile_client.shared.LiveDataList;
import be.kdg.mobile_client.user.UserRepository;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;
import lombok.Setter;

/**
 * The main viewmodel for joining a room and fetching its state.
 * It works similarly to a Presenter in the MVP pattern.
 * The LiveData variables are directly linked and accessible from the layout.
 * When the LiveData variables change, the bound data in the layout is subsequently changed.
 */
public class RoomViewModel extends ViewModel {
    private final RoomRepository roomRepo;
    private final RoundRepository roundRepo;
    private final UserRepository userRepo;
    @Getter LiveDataList<Act> acts = new LiveDataList<>();
    @Getter MutableLiveData<Room> room = new MutableLiveData<>();
    @Getter MutableLiveData<Round> round = new MutableLiveData<>();
    @Getter MutableLiveData<Player> player = new MutableLiveData<>();
    @Getter MutableLiveData<String> toast = new MutableLiveData<>();
    @Getter MutableLiveData<Boolean> myTurn = new MutableLiveData<>();
    @Getter MutableLiveData<List<ActType>> possibleActs = new MutableLiveData<>();
    @Getter @Setter MutableLiveData<Integer> seekBarValue = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Act lastAct;

    @Inject
    public RoomViewModel(RoomRepository roomRepo, RoundRepository roundRepo, UserRepository userRepo) {
        this.roomRepo = roomRepo;
        this.roundRepo = roundRepo;
        this.userRepo = userRepo;
    }

    /**
     * Setup initial room connection
     */
    void init(int roomId) {
        compositeDisposable.add(roomRepo.findById(roomId)
                .subscribe(next -> {
                    if (playerCapReached(next)) notifyUser(new Exception("Room is full."));
                    room.setValue(next);
                    roomRepo.listenOnRoomUpdate(roomId).subscribe(newRoom -> {
                        room.postValue(newRoom);
                        if (room.getValue().getPlayersInRoom().size() < 2) { // alone in room
                            updateRoomPlayers(newRoom.getPlayersInRoom());
                        }
                    }, this::notifyUser);
                    roundRepo.listenOnRoundUpdate(roomId).subscribe(newRound -> {
                        round.postValue(newRound);
                        updateRoomPlayers(newRound.getPlayersInRound());
                        checkTurns(newRound);
                    }, this::notifyUser);
                    //TODO: initializeWinnerConnection();
                    roomRepo.joinRoom(roomId).subscribe(player::postValue, this::notifyUser);
                    roomRepo.listenOnActUpdate(roomId).subscribe(this::onNewAct, this::notifyUser);
                }, this::notifyUser));
    }

    /**
     * Updates all players in the room with the new round.
     * The new round may contain new players.
     */
    private synchronized void updateRoomPlayers(List<Player> players) {
        Room tempRoom = room.getValue();
        tempRoom.setPlayersInRoom(players);
        for (Player roomPlayer : tempRoom.getPlayersInRoom()) {
            if (roomPlayer.getUserId().equals(player.getValue().getUserId())) {
                player.postValue(roomPlayer); // update self
            }
            if (roomPlayer.getUsername() == null) {
                compositeDisposable.add(userRepo.getUser(roomPlayer.getUserId()).subscribe(nextUser -> {
                    roomPlayer.setUsername(nextUser.getUsername());
                    room.postValue(tempRoom); // update room
                }, this::notifyUser));
            } else {
                room.postValue(tempRoom);
            }
        }
    }

    private void onNewAct(Act act) {
        lastAct = act;
        acts.add(act);
    }

    public String getBetByPhase(int index) {
        if (round.getValue() == null || room.getValue() == null || room.getValue().getPlayersInRoom().size() < index+1) return "0";
        Player roomPlayer = room.getValue().getPlayersInRoom().get(index);
        return String.valueOf(acts.getValue()
                .stream()
                .filter(a -> a.getPhase() == round.getValue().getCurrentPhase() && a.getUserId().equals(roomPlayer.getUserId()))
                .mapToInt(Act::getBet)
                .sum());
    }

    private void updatePossibleActs(int roundId) {
        compositeDisposable.add(roundRepo.getPossibleActs(roundId)
                .subscribe(possibleActs::postValue, this::notifyUser));
    }

    private void checkTurns(Round newRound) {
        if (lastAct != null) {
            updateTurns(lastAct.getNextUserId(), newRound);
        } else { // beginning of the round, no act has been played
            if (newRound == null) return; // round doesn't have 2 players
            int nextPlayerIndex = newRound.getButton() >= newRound.getPlayersInRound().size() - 1 ? 0 : newRound.getButton() + 1;
            updateTurns(newRound.getPlayersInRound().get(nextPlayerIndex).getUserId(), newRound);
        }
    }

    private void updateTurns(String userIdTurn, Round newRound) {
        Room tempRoom = room.getValue();
        tempRoom.getPlayersInRoom().forEach(roomPlayer -> {
            roomPlayer.setMyTurn(roomPlayer.getUserId().equals(userIdTurn));
            if (player.getValue().getUserId().equals(roomPlayer.getUserId())) {
                player.postValue(roomPlayer);
                myTurn.postValue(roomPlayer.isMyTurn());
                if (roomPlayer.isMyTurn()) updatePossibleActs(newRound.getId());
            }
        });
        room.setValue(tempRoom);
    }

    private boolean playerCapReached(Room newRoom) {
        return newRoom.getPlayersInRoom().size() >= newRoom.getGameRules().getMaxPlayerCount();
    }

    public boolean cardExists(int index) {
        return round.getValue() != null &&
                round.getValue().getCards() != null &&
                round.getValue().getCards().get(index) != null;
    }

    /**
     * This method is called when the user clicks on an act
     */
    public void onAct(ActType actType) {
        Player me = player.getValue();
        Round rnd = round.getValue();
        int roomId = room.getValue().getId();
        Act act = new Act(rnd.getId(), me.getUserId(), me.getId(), roomId, actType,
                rnd.getCurrentPhase(), 0, 0, "");
        if (actType == ActType.BET || actType == ActType.RAISE || actType == ActType.CALL) {
            final int bet = seekBarValue.getValue();
            act.setBet(bet);
            act.setTotalBet(bet);
        }
        compositeDisposable.add(roundRepo.addAct(act).subscribe(e -> seekBarValue.setValue(0), this::notifyUser));
    }

    /**
     * Update the toast livedata variable, RoomActivity listens on this variable and shows a toast
     */
    private void notifyUser(Throwable throwable) {
        toast.postValue(throwable.getMessage());
    }

    void leaveRoom() {
        compositeDisposable.add(roomRepo.leaveRoom(room.getValue().getId())
                .subscribe(e -> compositeDisposable.dispose(), this::notifyUser));
        roundRepo.disconnectWebsocket();
    }
}
