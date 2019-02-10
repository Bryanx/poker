package be.kdg.mobile_client.services;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Message;
import io.reactivex.Completable;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

/**
 * Websocket stomp service for sending and receiving chat messages.
 */
@SuppressLint("CheckResult")
public class ChatService {
    private final String URL = "ws://poker-game-service.herokuapp.com/chat/websocket";
    private final String SEND_ENDPOINT = "/chatroom/send/";
    private final String RECEIVE_ENDPOINT = "/chatroom/receive/";
    private final String SYSTEM_NAME = "system";
    private final String ERROR_TAG = "ChatService";
    private final int HEARTBEAT_MS = 10000;
    private StompClient stompClient;
    private String playerName;
    private int roomNumber;

    public void connect(int roomNumber, String playerName, Consumer<Throwable> onError) {
        this.roomNumber = roomNumber;
        this.playerName = playerName;
        this.stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL);
        stompClient.withClientHeartbeat(HEARTBEAT_MS).withServerHeartbeat(HEARTBEAT_MS);
        stompClient.connect();
        listenForLifeCycleChanges(onError);
    }

    public void sendMessage(String name, String message) {
        Message msg = new Message(name, message);
        String json = new Gson().toJson(msg);
        stompClient.send(SEND_ENDPOINT + roomNumber, json).subscribe();
    }

    public void setOnIncomingMessage(Consumer<StompMessage> onNext, Consumer<Throwable> onError) {
        stompClient.topic(RECEIVE_ENDPOINT + roomNumber).subscribe(onNext, onError);
    }

    private void listenForLifeCycleChanges(Consumer<Throwable> onError) {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    sendMessage(SYSTEM_NAME, playerName + " joined the room.");
                    break;
                case ERROR:
                    Log.e(ERROR_TAG, lifecycleEvent.getMessage(), lifecycleEvent.getException());
                    break;
                case CLOSED:
                    sendMessage(SYSTEM_NAME, playerName + " has left the room.");
                    break;
            }
        }, onError);
    }
}
