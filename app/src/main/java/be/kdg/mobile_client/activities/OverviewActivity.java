package be.kdg.mobile_client.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.RoomRecyclerAdapter;
import be.kdg.mobile_client.model.Room;
import be.kdg.mobile_client.services.RoomService;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * This activity is used to display all the rooms as cards.
 */
public class OverviewActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.lvUser) RecyclerView lvRoom;
    @BindView(R.id.tvOverviewHeader) TextView tvOverviewHeader;
    @Inject RoomService roomService;

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

    @SuppressLint("CheckResult")
    private void getRooms() {
        Observable<List<Room>> roomObs;

        if (getIntent().getStringExtra("TYPE").equals("PUBLIC")) {
            roomObs = roomService.getRooms().observeOn(AndroidSchedulers.mainThread());
            tvOverviewHeader.setText(getString(R.string.publicRooms).toUpperCase());
        } else {
            roomObs = roomService.getPrivateRooms().observeOn(AndroidSchedulers.mainThread());
            tvOverviewHeader.setText(getString(R.string.privateRooms).toUpperCase());
        }

        roomObs.subscribe(this::initializeAdapter, error -> Log.e("OverviewActivity", error.getMessage()));
    }

    /**
     * Initializes the rooms adapter to show all the rooms that
     * were retrieved from the game-service back-end.
     *
     * @param rooms The rooms that need to be used by the adapter.
     */
    private void initializeAdapter(List<Room> rooms) {
        System.out.println(Arrays.toString(rooms.toArray()));
        RoomRecyclerAdapter roomAdapter = new RoomRecyclerAdapter(this, rooms);
        lvRoom.setAdapter(roomAdapter);
        lvRoom.setLayoutManager(new LinearLayoutManager(this));
    }
}
