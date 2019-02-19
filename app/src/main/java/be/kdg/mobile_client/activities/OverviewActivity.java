package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.FriendAdapter;
import be.kdg.mobile_client.adapters.RoomAdapter;
import be.kdg.mobile_client.adapters.RoomRecyclerAdapter;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.services.CallbackWrapper;
import be.kdg.mobile_client.services.GameService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.lvRoom) RecyclerView lvRoom;
    @Inject GameService gameService;
    private RoomRecyclerAdapter roomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getControllerComponent().inject(this);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
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
               initializeAdapter(Arrays.asList(response.body()));
            } else {
                Toast.makeText(getApplicationContext(), "Error getting rooms", Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void initializeAdapter(List<Room> rooms) {
        roomAdapter = new RoomRecyclerAdapter(rooms);
        lvRoom.setAdapter(roomAdapter);
        lvRoom.setLayoutManager(new LinearLayoutManager(this));
    }
}
