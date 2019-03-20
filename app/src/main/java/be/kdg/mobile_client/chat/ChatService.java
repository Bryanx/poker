package be.kdg.mobile_client.chat;

import com.google.gson.Gson;

import javax.inject.Inject;

import be.kdg.mobile_client.shared.UrlService;
import be.kdg.mobile_client.shared.WebSocketService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Service for sending and receiving chat messages.
 */
public class ChatService {
    private final WebSocketService webSocketService;
    private int roomNumber;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ChatService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    void init(int roomNumber) {
        this.roomNumber = roomNumber;
        webSocketService.connect();
    }

    void sendMessage(String name, String message) {
        ChatMessage msg = new ChatMessage(name, message);
        String json = new Gson().toJson(msg);
        webSocketService.send(UrlService.chatRoomSend(roomNumber), json);
    }

    void setOnIncomingMessage(Consumer<ChatMessage> onNext, Consumer<Throwable> onError) {
        compositeDisposable.add(webSocketService.watch(UrlService.CHATROOM_RECEIVE_URL + roomNumber, ChatMessage.class)
                .subscribe(onNext, onError));
    }

    void disconnect() {
        compositeDisposable.dispose();
    }
}
