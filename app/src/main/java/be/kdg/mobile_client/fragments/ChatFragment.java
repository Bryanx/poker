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

import javax.inject.Inject;

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

@NoArgsConstructor // required for fragments
public class ChatFragment extends BaseFragment {
    @BindView(R.id.btnSend) Button btnSend;
    @BindView(R.id.etMessage) EditText etMessage;
    @BindView(R.id.lvChat) ListView lvChat;
    @Inject ChatService chatService;
    @Inject Gson gson;

    MessageAdapter messageAdapter;
    private String playerName = "Lotte";
    private int roomNumber = 1;
    private String ERROR_TAG = "ChatFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getControllerComponent().inject(this);
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);
        messageAdapter = new MessageAdapter(getActivity(), playerName);
        lvChat.setAdapter(messageAdapter);
        chatService.connect(roomNumber, playerName, (Throwable e) -> {
            getActivity().runOnUiThread(() -> messageAdapter.add(new Message("error", "Something wen't wrong.")));
            Log.e(ERROR_TAG, e.getMessage());
        });
        addEventHandlers();
        return view;
    }

    private void addEventHandlers() {
        //receive message
        chatService.setOnIncomingMessage((StompMessage msg) -> {
            Message message = gson.fromJson(msg.getPayload(), Message.class);
            getActivity().runOnUiThread(() -> messageAdapter.add(message));
        }, (Throwable error) -> Log.e(ERROR_TAG, error.getMessage()));
        //send message
        btnSend.setOnClickListener(e -> {
            if (etMessage.getText().length() > 0) {
                chatService.sendMessage(playerName, etMessage.getText().toString());
                etMessage.setText("");
            }
        });
    }
}
