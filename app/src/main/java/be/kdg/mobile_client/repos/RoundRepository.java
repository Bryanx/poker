package be.kdg.mobile_client.repos;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.model.Act;
import be.kdg.mobile_client.model.Round;
import be.kdg.mobile_client.services.RoundService;
import be.kdg.mobile_client.services.WebSocketService;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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

    public void addAct(Act act) {
        onErrorMsg = "Failed to play act";
        roundService.addAct(act)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next -> {}, this::logError);
    }

    public void listenOnRoundUpdate(int roomId, Consumer<Round> onUpdate) {
        onErrorMsg = "Could not receive round update, room: " + roomId;
        webSocketService.watch("/room/receive-round/" + roomId, Round.class)
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
