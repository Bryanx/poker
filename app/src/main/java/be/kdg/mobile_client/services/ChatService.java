package be.kdg.mobile_client.services;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Inject;

import be.kdg.mobile_client.model.Message;
import io.reactivex.functions.Consumer;
import lombok.RequiredArgsConstructor;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompMessage;

/**
 * Websocket stomp service for sending and receiving chat messages.
 */
@SuppressLint("CheckResult")
public class ChatService {
    private final WebSocketService webSocketService;
    private String playerName;
    private int roomNumber;

    @Inject
    public ChatService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void connect(int roomNumber, String playerName) {
        this.roomNumber = roomNumber;
        this.playerName = playerName;
    }

    public void sendMessage(String name, String message) {
        Message msg = new Message(name, message);
        String json = new Gson().toJson(msg);
        webSocketService.send("/chatrooms/" + roomNumber + "/send", json);
    }

    public void setOnIncomingMessage(Consumer<StompMessage> onNext, Consumer<Throwable> onError) {
        webSocketService.watch("/chatroom/receive/" + roomNumber, onNext, onError);
    }
}
