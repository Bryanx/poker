package be.kdg.mobile_client.services;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WebSocketService {
    private final StompClient stompClient;
    private static final String TAG = "WebSocketService";
    private final String URL = "wss://poker-game-service.herokuapp.com/connect/websocket";
    private final int HEARTBEAT_MS = 10000;

    @Inject
    public WebSocketService(StompClient stompClient) {
        this.stompClient = stompClient;
    }

    public void watch(String url, Consumer<StompMessage> onNext, Consumer<Throwable> onError) {
        stompClient.topic(url).subscribe(onNext, onError);
        Log.i(TAG, "Started listening on " + url + ". Stompclient is connected: " + stompClient.isConnected());
    }

    public void send(String url, String json) {
        stompClient.send(url, json).subscribe();
    }
}
