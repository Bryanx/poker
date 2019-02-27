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
                Log.i(TAG, "Succesfully fetched room: " + response.body().getId());
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
                Log.i(TAG, "Player: " + response.body().toString() +
                        " joined room: " + room.getValue().getId());
                getCurrentRound();
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
                    updatePlayers();
                },
                error -> handleError(error, "Could not receive room update: " + room.getValue().getId()));
    }

    private void initializeRoundConnection() {
        webSocketService.watch("/room/receive-round/" + room.getValue().getId(),
                next -> {
                    round.postValue(new Gson().fromJson(next.getPayload(), Round.class));
                    Room newRoom = room.getValue();
                    String newPlayers = round.getValue() != null ? String.valueOf(round.getValue().getPlayersInRound()) : "null";
                    Log.i(TAG, "Updating players in round: " + newRoom.getPlayersInRoom()
                            + " to: " + newPlayers);
                    newRoom.setPlayersInRoom(round.getValue().getPlayersInRound());
                    room.postValue(newRoom);
                },
                error -> handleError(error, "Could not receive round update: " + round.getValue()));
    }

    //TODO: This method is very bad, someone improve it.
    private void updatePlayers() {
        if (room.getValue() == null) return;
        if (room.getValue().getPlayersInRoom().get(0) != null) {
            binding.centerLayout.setPlayer0(room.getValue().getPlayersInRoom().get(0));
        }
        if (room.getValue().getPlayersInRoom().get(1) != null) {
            binding.centerLayout.setPlayer1(room.getValue().getPlayersInRoom().get(1));
        }
        if (room.getValue().getPlayersInRoom().get(2) != null) {
            binding.centerLayout.setPlayer2(room.getValue().getPlayersInRoom().get(2));
        }
        if (room.getValue().getPlayersInRoom().get(3) != null) {
            binding.centerLayout.setPlayer3(room.getValue().getPlayersInRoom().get(3));
        }
        if (room.getValue().getPlayersInRoom().get(4) != null) {
            binding.centerLayout.setPlayer4(room.getValue().getPlayersInRoom().get(4));
        }
        if (room.getValue().getPlayersInRoom().get(5) != null) {
            binding.centerLayout.setPlayer5(room.getValue().getPlayersInRoom().get(5));
        }
    }

    private boolean responseSuccess(Response response) {
        return response != null && response.body() != null && response.isSuccessful();
    }

    private void handleError(Throwable error, String msg) {
        if (error != null) Log.e(TAG, error.getMessage());
        Log.e(TAG, msg);
        message.postValue(msg);
    }

}
