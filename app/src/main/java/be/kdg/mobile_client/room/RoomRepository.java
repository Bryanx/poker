package be.kdg.mobile_client.room;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.room.model.Act;
import be.kdg.mobile_client.room.model.Player;
import be.kdg.mobile_client.room.model.Room;
import be.kdg.mobile_client.shared.UrlService;
import be.kdg.mobile_client.shared.WebSocketService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

/**
 * Repository class for all room related api calls.
 * Also includes websocket subscriptions.
 */
@Singleton
@SuppressLint("CheckResult")
public class RoomRepository {
    private static final String TAG = "RoomRepository";
    private static final String FAILED_TO_FETCH_ROOM = "Failed to fetch room";
    private static final String FAILED_TO_JOIN_ROOM = "Failed to join room";
    private static final String COULD_NOT_GET_CURRENT_ROUND = "Could not get current round";
    private static final String COULD_NOT_LEAVE_ROOM = "Could not leave room";
    private static final String COULD_NOT_RECEIVE_ROOM_UPDATE = "Could not receive room update: ";
    private static final String COULD_NOT_RECEIVE_WINNER = "Could not receive winner: ";
    private static final String COULD_NOT_RECEIVE_ROUND_UPDATE_ROOM = "Could not receive round update, room: ";
    private static final String SUCCESSFULLY_FETCHED_ROOM = "Successfully fetched room: ";
    private static final String PLAYER_JOINED_ROOM = "Player joined room: ";
    private final RoomService roomService;
    private final WebSocketService webSocketService;
    private String onErrorMsg;

    @Inject
    public RoomRepository(RoomService roomService, WebSocketService webSocketService) {
        this.roomService = roomService;
        this.webSocketService = webSocketService;
        webSocketService.connect();
    }

    synchronized Observable<Room> findById(int roomId) {
        onErrorMsg = FAILED_TO_FETCH_ROOM;
        return roomService.getRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach(each -> Log.i(TAG, SUCCESSFULLY_FETCHED_ROOM + each))
                .doOnError(this::logError);
    }

    synchronized Observable<Player> joinRoom(int roomId) {
        onErrorMsg = FAILED_TO_JOIN_ROOM;
        return roomService.joinRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError)
                .doOnEach(eachPlayer -> Log.i(TAG, PLAYER_JOINED_ROOM + roomId + ", " + eachPlayer.toString()));
    }

    private synchronized Observable<Response<Void>> getCurrentRound(int roomId) {
        onErrorMsg = COULD_NOT_GET_CURRENT_ROUND;
        return roomService.getCurrentRound(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    synchronized Observable<Response<Void>> leaveRoom(int roomId) {
        onErrorMsg = COULD_NOT_LEAVE_ROOM;
        return roomService.leaveRoom(roomId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    synchronized Flowable<Room> listenOnRoomUpdate(int roomId) {
        onErrorMsg = COULD_NOT_RECEIVE_ROOM_UPDATE + roomId;
        return webSocketService.watch(UrlService.RECEIVE_ROOM_URL + roomId, Room.class)
                .doOnError(this::logError)
                .doAfterNext(next -> getCurrentRound(roomId).subscribe(e->{},e->{}));
    }

    synchronized Flowable<Act> listenOnActUpdate(int roomId) {
        onErrorMsg = COULD_NOT_RECEIVE_ROUND_UPDATE_ROOM + roomId;
        return webSocketService.watch(UrlService.RECEIVE_ACT_URL + roomId, Act.class)
                .doOnError(this::logError);
    }

    Flowable<Player> listenForWinner(int roomId) {
        onErrorMsg = COULD_NOT_RECEIVE_WINNER + roomId;
        return webSocketService.watch(UrlService.RECEIVE_WINNER_URL + roomId, Player.class)
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
