package be.kdg.mobile_client.viewmodels;

import android.annotation.SuppressLint;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.ActType;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.repos.RoomRepository;
import be.kdg.mobile_client.repos.RoundRepository;
import lombok.Getter;

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

    @Inject
    public RoomViewModel(RoomRepository roomRepo, RoundRepository roundRepo) {
        this.roomRepo = roomRepo;
        this.roundRepo = roundRepo;
    }

    public void init(int roomId) {
        roomRepo.findById(roomId)
                .doOnError(error -> notification.postValue(error.getMessage()))
                .subscribe(next -> {
                    room.setValue(next);
                    if (playerCapReached(next)) return;
                    roomRepo.listenOnRoomUpdate(roomId).subscribe(room::postValue);
                    roundRepo.listenOnRoundUpdate(roomId).subscribe(round::postValue);
                    //TODO: initializeWinnerConnection();
                    roomRepo.joinRoom(roomId).subscribe(player::postValue);
                    roomRepo.listenOnActUpdate(roomId).subscribe(this::onNewAct);
                });
    }

    private void onNewAct(Act act) {
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
        roomRepo.leaveRoom(room.getValue().getId()).subscribe();
    }

    public void onAct(ActType actType) {
        Player me = player.getValue();
        Round rnd = round.getValue();
        int roomId = room.getValue().getId();
        Act act = new Act(rnd.getId(), me.getUserId(), me.getId(), roomId, actType,
                rnd.getCurrentPhase(), 0, 0, "");
        roundRepo.addAct(act).subscribe();
    }
}
