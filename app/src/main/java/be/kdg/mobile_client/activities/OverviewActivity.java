package be.kdg.mobile_client.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.RoomRecyclerAdapter;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.services.RoomService;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * This activity is used to display all the rooms as cards.
 */
@SuppressLint("CheckResult")
public class OverviewActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.lvUser) RecyclerView lvRoom;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @Inject RoomService roomService;
    @Inject UserService userService;

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
        btnBack.setOnClickListener(e -> navigateTo(MenuActivity.class));
    }

    private void getRooms() {
        roomService.getRooms().observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initializeAdapter, error -> {
                    Toast.makeText(this, "Failed to connect", Toast.LENGTH_LONG).show();
                    Log.e("OverviewActivity", error.getMessage());
                });
    }

    /**
     * Initializes the rooms adapter to show all the rooms that
     * were retrieved from the game-service back-end.
     *
     * @param rooms The rooms that need to be used by the adapter.
     */
    private void initializeAdapter(List<Room> rooms) {
        userService.getUser("").enqueue(new CallbackWrapper<>((throwable, myself) -> {
            progressBar.setVisibility(View.GONE);
            RoomRecyclerAdapter roomAdapter = new RoomRecyclerAdapter(this, rooms, myself.body());
            lvRoom.setAdapter(roomAdapter);
            lvRoom.setLayoutManager(new LinearLayoutManager(this));
        }));
    }
}
