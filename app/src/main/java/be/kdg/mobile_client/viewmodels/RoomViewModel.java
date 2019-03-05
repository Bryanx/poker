package be.kdg.mobile_client.viewmodels;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.SeekBar;

import javax.inject.Inject;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.ActType;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.repos.RoomRepository;
import be.kdg.mobile_client.repos.RoundRepository;
import be.kdg.mobile_client.services.SharedPrefService;
import lombok.Getter;
import lombok.Setter;

/**
 * Main viewmodel for joining a room and fetching its state.
 */
@SuppressLint("CheckResult")
public class RoomViewModel extends ViewModel {
    private final RoomRepository roomRepo;
    private final RoundRepository roundRepo;
    @Getter MutableLiveData<Room> room = new MutableLiveData<>();
    @Getter MutableLiveData<Round> round = new MutableLiveData<>();
    @Getter MutableLiveData<Player> player = new MutableLiveData<>();
    @Getter MutableLiveData<String> notification = new MutableLiveData<>();
    @Getter MutableLiveData<Boolean> myTurn = new MutableLiveData<>();
    @Getter ObservableField<String> seekBarValue = new ObservableField<>(0+"");

    @Inject
    public RoomViewModel(RoomRepository roomRepo, RoundRepository roundRepo) {
        this.roomRepo = roomRepo;
        this.roundRepo = roundRepo;
    }

    public void init(int roomId) {
        roomRepo.findById(roomId)
                .subscribe(next -> {
                    if (playerCapReached(next)) return;
                    room.setValue(next);
                    roomRepo.listenOnRoomUpdate(roomId).subscribe(room::postValue, error -> notification.postValue(error.getMessage()));
                    roundRepo.listenOnRoundUpdate(roomId).subscribe(value -> {
                        round.postValue(value);
                        updatePlayer(value);
                        checkTurnByBlinds(value);
                    }, error -> notification.postValue(error.getMessage()));
                    //TODO: initializeWinnerConnection();
                    roomRepo.joinRoom(roomId).subscribe(player::postValue, error -> notification.postValue(error.getMessage()));
                    roomRepo.listenOnActUpdate(roomId).subscribe(this::onNewAct, error -> notification.postValue(error.getMessage()));
                }, error -> notification.postValue(error.getMessage()));
    }

    private void updatePlayer(Round rnd) {
        rnd.getPlayersInRound().forEach(otherPlayer -> {
            if (otherPlayer.getUserId().equals(player.getValue().getUserId())) {
                player.postValue(otherPlayer);
            }
        });
    }

    /**
     * Check if its my turn
     */
    private void onNewAct(Act act) {
        myTurn.setValue(false);
        if (act.getNextUserId().equals(player.getValue().getUserId())) {
            myTurn.setValue(true);
        } else {
            checkTurnByBlinds(round.getValue());
        }
    }

    private void checkTurnByBlinds(Round rnd) {
        if (rnd == null) return;
        int nextPlayerIndex = rnd.getButton() >= rnd.getPlayersInRound().size() - 1 ? 0 : rnd.getButton() + 1;
        if (rnd.getPlayersInRound().get(nextPlayerIndex).getUserId().equals(player.getValue().getUserId())) {
            myTurn.setValue(true);
        }
    }

    private boolean playerCapReached(Room newRoom) {
        return newRoom.getPlayersInRoom().size() >= newRoom.getGameRules().getMaxPlayerCount();
    }

    public boolean cardExists(int index) {
        return round.getValue() != null &&
                round.getValue().getCards() != null &&
                round.getValue().getCards().get(index) != null;
    }

    public void leaveRoom() {
        roomRepo.leaveRoom(room.getValue().getId()).subscribe(e ->{}, e -> {});
    }

    public void onAct(ActType actType) {
        Player me = player.getValue();
        Round rnd = round.getValue();
        int roomId = room.getValue().getId();
        Act act = new Act(rnd.getId(), me.getUserId(), me.getId(), roomId, actType,
                rnd.getCurrentPhase(), 0, 0, "");
        roundRepo.addAct(act).subscribe(e ->{}, e -> {});
    }

    public void onValueChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
        seekBarValue.set(progresValue + "");
    }
}
