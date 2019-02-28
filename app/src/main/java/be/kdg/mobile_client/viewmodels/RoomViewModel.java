package be.kdg.mobile_client.viewmodels;

import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.databinding.ActivityRoomBinding;
import be.kdg.mobile_client.databinding.RoomCenterBinding;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.WebSocketService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import retrofit2.Response;

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
    private ActivityRoomBinding binding;

    @Inject
    public RoomViewModel(WebSocketService webSocketService, GameService gameService) {
        this.webSocketService = webSocketService;
        this.gameService = gameService;
    }

    public void init(int roomNumber, ActivityRoomBinding bind) {
        binding = bind;
        binding.centerLayout.setViewmodel(this);
        gameService.getRoom(roomNumber).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                Log.i(TAG, "Succesfully fetched room: " + response.body());
                room.setValue(response.body());
                checkPlayerCap();
                initializeRoomConnection();
                initializeRoundConnection();
                //TODO: initializeWinnerConnection();
                joinRoom();
            } else {
                handleError(throwable, "Failed to fetch room");
            }
        }));
    }

    private void checkPlayerCap() {
        if (room.getValue().getPlayersInRoom().size() >= room.getValue().getGameRules().getMaxPlayerCount()) {
            //TODO: navigate to home
        }
    }

    private void joinRoom() {
        gameService.joinRoom(room.getValue().getId()).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                player.postValue(response.body());
                Log.i(TAG, "Player: " + response.body().toString() + " joined room: " + room.getValue());
            } else {
                handleError(throwable, "Failed to join room");
            }
        }));
    }

    private void getCurrentRound() {
        gameService.getCurrentRound(room.getValue().getId()).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                if (throwable != null) Log.e(TAG, throwable.getMessage());
                Log.i(TAG, "Getting current round.");
            } else {
                handleError(throwable, "Failed to get current round");
            }
        }));
    }

    private void initializeRoomConnection() {
        webSocketService.watch("/room/receive-room/" + room.getValue().getId(),
                next -> {
                    Log.i(TAG, "Received room update: " + room.getValue().getId());
                    room.postValue(new Gson().fromJson(next.getPayload(), Room.class));
                    getCurrentRound();
                },
                error -> handleError(error, "Could not receive room update: " + room.getValue().getId()));
    }

    private void initializeRoundConnection() {
        webSocketService.watch("/room/receive-round/" + room.getValue().getId(),
                next -> {
                    round.postValue(new Gson().fromJson(next.getPayload(), Round.class));
                    Log.i(TAG, "Updating round to: " + next.getPayload());
                },
                error -> handleError(error, "Could not receive round update: " + round.getValue()));
    }

    private boolean responseSuccess(Response response) {
        return response != null && response.body() != null && response.isSuccessful();
    }

    private void handleError(Throwable error, String msg) {
        if (error != null) Log.e(TAG, error.getMessage());
        Log.e(TAG, msg);
        message.postValue(msg);
    }

    public boolean cardExists(int index) {
        return round.getValue() != null &&
                round.getValue().getCards() != null &&
                round.getValue().getCards().get(index) != null;
    }

}
