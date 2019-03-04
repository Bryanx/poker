package be.kdg.mobile_client.repos;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.services.RoundService;
import be.kdg.mobile_client.services.WebSocketService;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Response;

@Singleton
@SuppressLint("CheckResult")
public class RoundRepository {
    private static final String TAG = "RoundRepository";
    private String onErrorMsg;
    private final WebSocketService webSocketService;
    private final RoundService roundService;

    @Inject
    public RoundRepository(WebSocketService webSocketService, RoundService roundService) {
        this.webSocketService = webSocketService;
        this.roundService = roundService;
    }

    public Observable<Response<Void>> addAct(Act act) {
        onErrorMsg = "Failed to play act";
        return roundService.addAct(act)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(this::logError);
    }

    public Flowable<Round> listenOnRoundUpdate(int roomId) {
        onErrorMsg = "Could not receive round update, room: " + roomId;
        return webSocketService.watch("/room/receive-round/" + roomId, Round.class)
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
