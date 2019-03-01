package be.kdg.mobile_client.viewmodels;

import java.util.Optional;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.repos.RoomRepository;
import lombok.Getter;

/**
 * Main viewmodel for joining a room and fetching its state.
 */
public class RoomViewModel extends ViewModel {
    private final RoomRepository roomRepo;
    @Getter MutableLiveData<Room> room = new MutableLiveData<>();
    @Getter MutableLiveData<Round> round = new MutableLiveData<>();
    @Getter MutableLiveData<Player> player = new MutableLiveData<>();
    @Getter MutableLiveData<String> notification = new MutableLiveData<>();
    private MutableLiveData<Act> lastAct = new MutableLiveData<>();

    @Inject
    public RoomViewModel(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public void init(int roomId) {
        roomRepo.getRoom(roomId, next -> {
            room.setValue(next);
            if (playerCapReached(next)) return;
            roomRepo.listenOnRoomUpdate(roomId, room::postValue);
            roomRepo.listenOnRoundUpdate(roomId, round::postValue);
            //TODO: initializeWinnerConnection();
            roomRepo.joinRoom(roomId, player::postValue);
            roomRepo.listenOnActUpdate(roomId, this::onNewAct);
        }, notification::postValue);
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
        roomRepo.leaveRoom(room.getValue().getId());
    }

    public void onCall() {
    }

    public void onFold() {

    }
}
