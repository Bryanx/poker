package be.kdg.mobile_client.room;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.room.model.Act;
import be.kdg.mobile_client.room.model.ActType;
import be.kdg.mobile_client.room.model.Player;
import be.kdg.mobile_client.room.model.Room;
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
    @Getter MutableLiveData<List<ActType>> possibleActs = new MutableLiveData<>();
    @Getter @Setter MutableLiveData<Integer> seekBarValue = new MutableLiveData<>();
    @Getter ObservableBoolean loading = new ObservableBoolean(true);
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
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
                    if (playerCapReached(next)) notifyUser(new Exception(App.getContext().getString(R.string.room_full)));
                    room.setValue(next);
                    roomRepo.listenOnRoomUpdate(roomId).subscribe(this::onRoomUpdate, this::notifyUser);
                    roundRepo.listenOnRoundUpdate(roomId).subscribe(this::onRoundUpdate, this::notifyUser);
                    roomRepo.listenForWinner(roomId).subscribe(resp -> acts.clear(), this::notifyUser);
                    roomRepo.joinRoom(roomId).subscribe(this::onJoinRoom, this::notifyUser);
                    roomRepo.listenOnActUpdate(roomId).subscribe(this::onNewAct, this::notifyUser);
                }, this::notifyUser));
    }

    private void onRoomUpdate(Room newRoom) {
        room.postValue(newRoom);
        if (room.getValue().getPlayersInRoom().size() < 2) { // alone in room
            updatePlayersInRoom(null, newRoom.getPlayersInRoom());
        }
    }

    private void onJoinRoom(Player value) {
        loading.set(false);
        player.postValue(value);
    }

    private void onRoundUpdate(Round newRound) {
        Round oldRound = round.getValue();
        round.postValue(newRound);
        updatePlayersInRoom(oldRound == null ? null : oldRound.getPlayersInRound(), newRound.getPlayersInRound());
        checkTurns(newRound);
    }

    /**
     * Updates all players in the room.
     * The list of oldplayers is used to prevent doing api calls every round to fetch the usernames.
     */
    private synchronized void updatePlayersInRoom(List<Player> oldPlayers, List<Player> newPlayers) {
        Room tempRoom = room.getValue();
        tempRoom.setPlayersInRoom(newPlayers);
        for (Player roomPlayer : tempRoom.getPlayersInRoom()) {
            if (roomPlayer.equals(player.getValue())) player.postValue(roomPlayer); // update self
            if (oldPlayers != null) {
                Optional<Player> oldPlayer = oldPlayers.stream().filter(roomPlayer::equals).findFirst();
                if (oldPlayer.isPresent()) roomPlayer.setUsername(oldPlayer.get().getUsername());
                else updateUsername(tempRoom, roomPlayer);
                room.postValue(tempRoom);
            } else {
                updateUsername(tempRoom, roomPlayer);
            }
        }
    }

    private void updateUsername(Room tempRoom, Player roomPlayer) {
        compositeDisposable.add(userRepo.getUser(roomPlayer.getUserId()).subscribe(nextUser -> {
            roomPlayer.setUsername(nextUser.getUsername());
            room.postValue(tempRoom);
        }, this::notifyUser));
    }

    private void onNewAct(Act act) {
        lastAct = act;
        acts.add(act);
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
            if (roomPlayer.equals(player.getValue())) {
                player.postValue(roomPlayer);
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
    public synchronized void onAct(ActType actType) {
        if (!player.getValue().isMyTurn()) return;
        Player me = player.getValue();
        me.setMyTurn(false);
        player.setValue(me);
        Round rnd = round.getValue();
        int roomId = room.getValue().getId();
        Act act = new Act(rnd.getId(), me.getUserId(), me.getId(), roomId, actType,
                rnd.getCurrentPhase(), 0, 0, "");
        if (actType == ActType.BET || actType == ActType.RAISE) {
            final int bet = seekBarValue.getValue();
            act.setBet(bet);
            act.setTotalBet(bet);
        } else if (actType == ActType.CALL) {
            act.setBet(getLastHighestBet() - getLastPlayerBet(player.getValue()));
        }
        compositeDisposable.add(roundRepo.addAct(act).subscribe(e -> seekBarValue.setValue(0), this::notifyUser));
    }

    public String getBetByPhase(int index) {
        if (round.getValue() == null || room.getValue() == null) return "0";
        Player roomPlayer = getPlayerBySeat(index);
        if (roomPlayer == null) return "";
        return String.valueOf(getLastPlayerBet(roomPlayer));
    }

    private int getLastPlayerBet(Player player) {
        return acts.getValue()
                .stream()
                .filter(act -> act.getPhase() == round.getValue().getCurrentPhase())
                .filter(act -> act.getUserId().equals(player.getUserId()))
                .mapToInt(Act::getBet)
                .sum();
    }

    public int getLastHighestBet() {
        return room.getValue().getPlayersInRoom()
                .stream()
                .mapToInt(this::getLastPlayerBet)
                .max()
                .orElse(0);
    }

    public Player getPlayerBySeat(int seatNr) {
        final int nr = seatNr + 1;
        return room.getValue().getPlayersInRoom()
                .stream()
                .filter(play -> play.getSeatNumber() == nr)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns which button is active for a player (D, SB or BB) based on the seatnumber.
     */
    public String getButton(int seatNr) {
        if (round.getValue() == null) return "";
        if (seatNr == round.getValue().getButton()) return App.getContext().getString(R.string.dealer);
        if (seatNr == round.getValue().getSmallBlind()) return App.getContext().getString(R.string.small_blind);
        if (seatNr == round.getValue().getBigBlind()) return App.getContext().getString(R.string.big_blind);
        return "";
    }

    /**
     * Called when turn timer is finished
     */
    public void onTimerFinished(Player finishedPlayer) {
        if (player.getValue() != null && finishedPlayer != null && finishedPlayer.equals(player.getValue())) {
            onAct(ActType.FOLD);
        }
    }

    /**
     * Update the toast livedata variable, RoomActivity listens on this variable and shows a toast
     */
    private void notifyUser(Throwable throwable) {
        toast.postValue(throwable.getMessage());
    }

    void leaveRoom() {
        if (room.getValue() == null) return;
        compositeDisposable.add(roomRepo.leaveRoom(room.getValue().getId())
                .subscribe(e -> compositeDisposable.dispose(), this::notifyUser));
        roundRepo.disconnectWebsocket();
    }
}
