package be.kdg.mobile_client;

import android.annotation.SuppressLint;
import android.net.wifi.aware.WifiAwareSession;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import be.kdg.mobile_client.model.Message;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class RoomActivity extends AppCompatActivity {
    private String url = "wss://poker-game-service.herokuapp.com/chat/websocket";
    private String playerName = "Lotte";
    private int roomNumber = 1;
    private OkHttpClient client = new OkHttpClient();
    ;
    @BindView(R.id.btnSend)
    Button btnSend;
    @BindView(R.id.etMessage)
    EditText etMessage;
    @BindView(R.id.tvChat)
    TextView tvChat;
    private StompClient mStompClient;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        ButterKnife.bind(this);
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        mStompClient.connect();

        mStompClient.topic("/chatroom/receive/" + roomNumber).subscribe(msg -> {
            Message message = new Gson().fromJson(msg.getPayload(), Message.class);
            tvChat.setText(tvChat.getText() + "\n" + message.getName() + ": " + message.getContent());
        });

        btnSend.setOnClickListener(e -> {
            if (etMessage.getText().length() > 0) {
                send(playerName, etMessage.getText().toString());
                etMessage.setText("");
            }
        });

        mStompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    send("system", playerName + " joined the room.");
                    break;
                case ERROR:
                    Log.e("ERROR", "Error", lifecycleEvent.getException());
                    break;
                case CLOSED:
                    send("system", playerName + " has left the room.");
                    break;
            }
        });
    }

    private void send(String name, String message) {
        Message msg = new Message(name, message);
        String json = new Gson().toJson(msg);
        mStompClient.send("/chatroom/send/" + roomNumber, json).subscribe();
    }

    @Override
    protected void onDestroy() {
        send("system", playerName + " has left the room.");
        mStompClient.disconnect();
        super.onDestroy();
    }
}
