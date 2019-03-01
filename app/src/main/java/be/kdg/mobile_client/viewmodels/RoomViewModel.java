package be.kdg.mobile_client.viewmodels;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.databinding.ActivityRoomBinding;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.WebSocketService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lombok.Getter;

/**
 * Main viewmodel for joining a room and fetching its state.
 */
public class RoomViewModel extends ViewModel {
    private final String TAG = "RoomViewModel";
    private final WebSocketService webSocketService;
    private final GameService gameService;
    @Getter MutableLiveData<Room> room = new MutableLiveData<>();
    @Getter MutableLiveData<Round> round = new MutableLiveData<>();
    @Getter MutableLiveData<Player> player = new MutableLiveData<>();
    @Getter MutableLiveData<String> message = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public RoomViewModel(WebSocketService webSocketService, GameService gameService) {
        this.webSocketService = webSocketService;
        this.gameService = gameService;
    }

    public void init(int roomNumber) {
        disposables.add(gameService.getRoom(roomNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {
                    Log.i(TAG, "Succesfully fetched room: " + next);
                    room.setValue(next);
                    checkPlayerCap();
                    initializeRoomConnection();
                    initializeRoundConnection();
                    //TODO: initializeWinnerConnection();
                    joinRoom();
                }, error -> handleError(error, "Failed to fetch room")));
    }

    private void checkPlayerCap() {
        if (room.getValue().getPlayersInRoom().size() >= room.getValue().getGameRules().getMaxPlayerCount()) {
            //TODO: navigate to home
        }
    }

    private void joinRoom() {
        disposables.add(gameService.joinRoom(room.getValue().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(each -> Log.i(TAG, "Player: " + each.toString() + " joined room: " + room.getValue()))
                .subscribe(next -> player.postValue(next),
                        error -> handleError(error, "Failed to join room")));
    }

    private void getCurrentRound() {
        disposables.add(gameService.getCurrentRound(room.getValue().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {}, error -> handleError(error, "Could not get current round")));
    }

    private void initializeRoomConnection() {
        disposables.add(webSocketService.watch("/room/receive-room/" + room.getValue().getId(), Room.class)
                .doAfterNext(next -> getCurrentRound())
                .subscribe(next -> room.postValue(next),
                        error -> handleError(error, "Could not receive room update: " + room.getValue().getId())));
    }

    private void initializeRoundConnection() {
        disposables.add(webSocketService.watch("/room/receive-round/" + room.getValue().getId(), Round.class)
                .subscribe(next -> round.postValue(next),
                        error -> handleError(error, "Could not receive round update: " + round.getValue())));
    }

    private void handleError(Throwable error, String msg) {
        Log.e(TAG, msg);
        message.postValue(msg);
        if (error != null) {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        }
    }

    public boolean cardExists(int index) {
        return round.getValue() != null &&
                round.getValue().getCards() != null &&
                round.getValue().getCards().get(index) != null;
    }

    public void leaveRoom() {
        disposables.add(gameService.leaveRoom(room.getValue().getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {}, error -> handleError(error, "Could not leave room")));
        disposables.clear();
    }
}
