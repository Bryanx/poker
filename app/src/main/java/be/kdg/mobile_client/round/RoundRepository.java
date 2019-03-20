package be.kdg.mobile_client.round;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.room.model.Act;
import be.kdg.mobile_client.room.model.ActType;
import be.kdg.mobile_client.shared.UrlService;
import be.kdg.mobile_client.shared.WebSocketService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

/**
 * Repository class for all round related api calls.
 * Also includes websocket subscriptions.
 */
@Singleton
@SuppressLint("CheckResult")
public class RoundRepository {
    private static final String TAG = "RoundRepository";
    private static final String FAILED_TO_PLAY_ACT = "Failed to play act";
    private static final String FAILED_TO_GET_POSSIBLE_ACTS = "Failed to get possible acts";
    private static final String COULD_NOT_RECEIVE_ROUND_UPDATE = "Could not receive round update, room: ";
    private String onErrorMsg;
    private final WebSocketService webSocketService;
    private final RoundService roundService;

    @Inject
    public RoundRepository(WebSocketService webSocketService, RoundService roundService) {
        this.webSocketService = webSocketService;
        this.roundService = roundService;
        webSocketService.connect();
    }

    public synchronized Observable<Response<Void>> addAct(Act act) {
        onErrorMsg = FAILED_TO_PLAY_ACT;
        return roundService.addAct(act)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    public synchronized Observable<List<ActType>> getPossibleActs(int roundId) {
        onErrorMsg = FAILED_TO_GET_POSSIBLE_ACTS;
        return roundService.getPossibleActs(String.valueOf(roundId))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    public synchronized Flowable<Round> listenOnRoundUpdate(int roomId) {
        onErrorMsg = COULD_NOT_RECEIVE_ROUND_UPDATE + roomId;
        return webSocketService.watch(UrlService.RECEIVE_ROUND_URL + roomId, Round.class)
                .doOnError(this::logError);
    }

    private void logError(Throwable error) {
        Log.e(TAG, onErrorMsg);
        if (error != null) {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        }
    }

    public void disconnectWebsocket() {
        webSocketService.disconnect();
    }
}
