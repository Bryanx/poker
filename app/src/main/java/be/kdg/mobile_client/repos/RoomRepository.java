package be.kdg.mobile_client.repos;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.WebSocketService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

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

    public Observable<Room> findById(int roomId) {
        onErrorMsg = "Failed to fetch room";
        return gameService.getRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(each -> Log.i(TAG, "Succesfully fetched room: " + each))
                .doOnError(this::logError);
    }

    public Observable<Player> joinRoom(int roomId) {
        onErrorMsg = "Failed to join room";
        return gameService.joinRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError)
                .doOnEach(eachPlayer -> Log.i(TAG, "Joined room: " + roomId + " player: " + eachPlayer.toString()));
    }

    public Observable<Response<Void>> getCurrentRound(int roomId) {
        onErrorMsg = "Could not get current round";
        return gameService.getCurrentRound(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    public Observable<Response<Void>> leaveRoom(int roomId) {
        onErrorMsg = "Could not leave room";
        return gameService.leaveRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    public Flowable<Room> listenOnRoomUpdate(int roomId) {
        onErrorMsg = "Could not receive room update: " + roomId;
        return webSocketService.watch("/room/receive-room/" + roomId, Room.class)
                .doOnError(this::logError)
                .doAfterNext(next -> getCurrentRound(roomId).subscribe());
    }

    public Flowable<Act> listenOnActUpdate(int roomId) {
        onErrorMsg = "Could not receive round update, room: " + roomId;
        return webSocketService.watch("/room/receive-act/" + roomId, Act.class)
                .doOnError(this::logError);
    }

    private void logError(Throwable error) {
        Log.e(TAG, onErrorMsg);
        if (error != null) {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        }
    }
}
