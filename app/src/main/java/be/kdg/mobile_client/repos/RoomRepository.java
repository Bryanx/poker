package be.kdg.mobile_client.repos;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.WebSocketService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

@Singleton
@SuppressLint("CheckResult")
public class RoomRepository {
    private static final String TAG = "RoomRepository";
    private final GameService gameService;
    private final WebSocketService webSocketService;
    private String onErrorMsg;

    @Inject
    public RoomRepository(GameService gameService, WebSocketService webSocketService) {
        this.gameService = gameService;
        this.webSocketService = webSocketService;
    }

    public void getRoom(int roomId, Consumer<Room> onNext, Consumer<String> onError) {
        onErrorMsg = "Failed to fetch room";
        gameService.getRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(each -> Log.i(TAG, "Succesfully fetched room: " + each))
                .doOnError(error -> onError.accept(onErrorMsg)) //after logging do this
                .subscribe(onNext, this::logError);
    }

    public void joinRoom(int roomId, Consumer<Player> onJoin) {
        onErrorMsg = "Failed to join room";
        gameService.joinRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(eachPlayer -> Log.i(TAG, "Joined room: " + roomId + " player: " + eachPlayer.toString()))
                .subscribe(onJoin, this::logError);
    }

    public void getCurrentRound(int roomId) {
        onErrorMsg = "Could not get current round";
        gameService.getCurrentRound(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {}, this::logError);
    }

    public void leaveRoom(int roomId) {
        onErrorMsg = "Could not leave room";
        gameService.leaveRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {}, this::logError);
    }

    public void listenOnRoomUpdate(int roomId, Consumer<Room> onUpdate) {
        onErrorMsg = "Could not receive room update: " + roomId;
        webSocketService.watch("/room/receive-room/" + roomId, Room.class)
                .doAfterNext(next -> getCurrentRound(roomId))
                .subscribe(onUpdate, this::logError);
    }

    public void listenOnRoundUpdate(int roomId, Consumer<Round> onUpdate) {
        onErrorMsg = "Could not receive round update, room: " + roomId;
        webSocketService.watch("/room/receive-round/" + roomId, Round.class)
                .subscribe(onUpdate, this::logError);
    }

    public void listenOnActUpdate(int roomId, Consumer<Act> onUpdate) {
        onErrorMsg = "Could not receive round update, room: " + roomId;
        webSocketService.watch("/room/receive-act/" + roomId, Act.class)
                .subscribe(onUpdate, this::logError);
    }

    private void logError(Throwable error) {
        Log.e(TAG, onErrorMsg);
        if (error != null) {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        }
    }
}
