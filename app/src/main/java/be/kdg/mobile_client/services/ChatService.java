package be.kdg.mobile_client.services;

import com.google.gson.Gson;

import javax.inject.Inject;

import be.kdg.mobile_client.model.Message;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.dto.StompMessage;

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
        Message msg = new Message(name, message);
        String json = new Gson().toJson(msg);
        webSocketService.send("/chatrooms/" + roomNumber + "/send", json);
    }

    public void setOnIncomingMessage(Consumer<Message> onNext, Consumer<Throwable> onError) {
        chatDisposable = webSocketService.watch("/chatroom/receive/" + roomNumber, Message.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    public void disconnect() {
        chatDisposable.dispose();
    }
}
