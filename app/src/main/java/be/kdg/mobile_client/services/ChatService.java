package be.kdg.mobile_client.services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;

import be.kdg.mobile_client.model.Message;
import io.reactivex.functions.Consumer;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

/**
 * Websocket stomp service for sending and receiving chat messages.
 */
@SuppressLint("CheckResult")
public class ChatService {
    private final String URL = "wss://poker-game-service.herokuapp.com/chat/websocket";
    private final String SEND_ENDPOINT = "/chatroom/send/";
    private final String RECEIVE_ENDPOINT = "/chatroom/receive/";
    private final String JOIN_MESSAGE = " joined the room.";
    private final String LEAVE_MESSAGE = " has left the room.";
    private final String SYSTEM_NAME = "system";
    private final String ERROR_TAG = "ERROR";
    private final int HEARTBEAT_MS = 1000;
    private StompClient stompClient;
    private String playerName;
    private int roomNumber;

    public void connect(int roomNumber, String playerName) {
        this.roomNumber = roomNumber;
        this.playerName = playerName;
        this.stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL);
        stompClient.withClientHeartbeat(HEARTBEAT_MS).withServerHeartbeat(HEARTBEAT_MS);
        stompClient.connect();
        listenForLifeCycleChanges();
    }

    public void sendMessage(String name, String message) {
        Message msg = new Message(name, message);
        String json = new Gson().toJson(msg);
        stompClient.send(SEND_ENDPOINT + roomNumber, json).subscribe();
    }

    public void setOnIncomingMessage(Consumer<StompMessage> consumer) {
        stompClient.topic(RECEIVE_ENDPOINT + roomNumber).subscribe(consumer);
    }

    private void listenForLifeCycleChanges() {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    sendMessage(SYSTEM_NAME, playerName + JOIN_MESSAGE);
                    break;
                case ERROR:
                    Log.e(ERROR_TAG, lifecycleEvent.getMessage(), lifecycleEvent.getException());
                    break;
                case CLOSED:
                    sendMessage(SYSTEM_NAME, playerName + LEAVE_MESSAGE);
                    break;
            }
        }, e -> Log.e(ERROR_TAG, e.getMessage()));
    }

    public void disconnect() {
        sendMessage(SYSTEM_NAME, playerName + LEAVE_MESSAGE);
        stompClient.disconnect();
    }
}
