package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.RoomRecyclerAdapter;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.shared.CallbackWrapper;
import be.kdg.mobile_client.services.GameService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity is used to display all the rooms as cards.
 */
public class OverviewActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.lvUser) RecyclerView lvRoom;
    @Inject GameService gameService;

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
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        });
    }

    private void getRooms() {
        gameService.getRooms().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response != null && response.isSuccessful() && response.body() != null) {
               initializeAdapter(response.body());
            } else {
                final String msg = throwable == null ? "" : throwable.getMessage();
                Log.e("OverviewActivity", "Error getting rooms. " + msg);
                Toast.makeText(getApplicationContext(), getString(R.string.error_getting_rooms), Toast.LENGTH_LONG).show();
            }
        }));
    }

    /**
     * Initializes the rooms adapter to show all the rooms that
     * were retrieved from the game-service back-end.
     *
     * @param rooms The rooms that need to be used by the adapter.
     */
    private void initializeAdapter(List<Room> rooms) {
        RoomRecyclerAdapter roomAdapter = new RoomRecyclerAdapter(this, rooms);
        lvRoom.setAdapter(roomAdapter);
        lvRoom.setLayoutManager(new LinearLayoutManager(this));
    }
}
