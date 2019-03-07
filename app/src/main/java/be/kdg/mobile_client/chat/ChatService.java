package be.kdg.mobile_client.chat;

import com.google.gson.Gson;

import javax.inject.Inject;

import be.kdg.mobile_client.shared.WebSocketService;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Service for sending and receiving chat messages.
 */
public class ChatService {
    private final WebSocketService webSocketService;
    private int roomNumber;
    private Disposable chatDisposable;

    @Inject
    public ChatService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void init(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void sendMessage(String name, String message) {
        ChatMessage msg = new ChatMessage(name, message);
        String json = new Gson().toJson(msg);
        webSocketService.send("/chatrooms/" + roomNumber + "/send", json);
    }

    public void setOnIncomingMessage(Consumer<ChatMessage> onNext, Consumer<Throwable> onError) {
        chatDisposable = webSocketService.watch("/chatroom/receive/" + roomNumber, ChatMessage.class)
                .subscribe(onNext, onError);
    }

    public void disconnect() {
        chatDisposable.dispose();
    }
}
