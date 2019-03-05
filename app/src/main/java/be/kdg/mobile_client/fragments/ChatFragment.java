package be.kdg.mobile_client.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.MessageAdapter;
import be.kdg.mobile_client.model.Message;
import be.kdg.mobile_client.services.ChatService;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NoArgsConstructor;
import ua.naiksoftware.stomp.dto.StompMessage;

/**
 * Fragment in which a chat conversation is shown
 */
@NoArgsConstructor // required for fragments
public class ChatFragment extends BaseFragment {
    @BindView(R.id.btnSend) Button btnSend;
    @BindView(R.id.etMessage) EditText etMessage;
    @BindView(R.id.lvChat) ListView lvChat;
    private ChatService chatService;

    private MessageAdapter messageAdapter;
    private String username;
    private final String ERROR_TAG = "ChatFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        messageAdapter = new MessageAdapter(getActivity());
        lvChat.setAdapter(messageAdapter);
        return view;
    }

    /**
     * Connect to the chat service and log errors to the adapter.
     */
    public void connectChat(int roomNumber, ChatService chatService, String username) {
        this.username = username;
        messageAdapter.setName(username);
        this.chatService = chatService;
        chatService.init(roomNumber);
        addEventHandlers();
        chatService.sendMessage("system", username + " joined the room");
    }

    /**
     * Handle incoming and outgoing messages
     */
    private void addEventHandlers() {
        chatService.setOnIncomingMessage(
                msg -> messageAdapter.add(msg),
                error -> Log.e(ERROR_TAG, error.getMessage()));
        btnSend.setOnClickListener(e -> {
            if (etMessage.getText().length() > 0) {
                chatService.sendMessage(username, etMessage.getText().toString());
                etMessage.setText("");
            }
        });
    }

    public void leaveChat() {
        chatService.sendMessage("system", username + " has left the room");
        chatService.disconnect();
    }
}
