package be.kdg.mobile_client.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Message;
import be.kdg.mobile_client.services.ChatService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RoomActivity extends BaseActivity {
    @BindView(R.id.btnSend) Button btnSend;
    @BindView(R.id.etMessage) EditText etMessage;
    @BindView(R.id.tvChat) TextView tvChat;
    @Inject ChatService chatService;
    @Inject Gson gson;

    private String playerName = "Lotte";
    private int roomNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        chatService.connect(roomNumber, playerName);
        addEventHandlers();
    }

    private void addEventHandlers() {
        chatService.setOnIncomingMessage(msg -> {
            Message message = gson.fromJson(msg.getPayload(), Message.class);
            tvChat.setText(tvChat.getText() + "\n" + message.getName() + ": " + message.getContent());
        });
        btnSend.setOnClickListener(e -> {
            if (etMessage.getText().length() > 0) {
                chatService.sendMessage(playerName, etMessage.getText().toString());
                etMessage.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        chatService.disconnect();
        super.onDestroy();
    }
}
