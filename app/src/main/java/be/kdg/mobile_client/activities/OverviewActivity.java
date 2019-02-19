package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.FriendAdapter;
import be.kdg.mobile_client.adapters.RoomAdapter;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.services.CallbackWrapper;
import be.kdg.mobile_client.services.GameService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.lvRoom) ListView lvRoom;
    @Inject GameService gameService;
    private RoomAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getControllerComponent().inject(this);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);

        roomAdapter = new RoomAdapter(this);
        lvRoom.setAdapter(roomAdapter);
        addEventHandlers();
        getRooms();
    }

    private void addEventHandlers() {
        btnBack.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
            startActivity(intent);
        });
    }

    private void getRooms() {
        gameService.getRooms().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                for (Room room : response.body()) {
                    roomAdapter.add(room);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error getting rooms", Toast.LENGTH_LONG).show();
            }
        }));
    }
}
