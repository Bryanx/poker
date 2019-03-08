package be.kdg.mobile_client.room.overview;

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
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.room.Room;
import be.kdg.mobile_client.room.RoomService;
import be.kdg.mobile_client.user.UserService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * This activity is used to display all the rooms as cards.
 */
@SuppressLint("CheckResult")
public class RoomOverviewActivity extends BaseActivity {
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
        Observable<List<Room>> roomObs;

        if (getIntent().getStringExtra("TYPE").equals("PUBLIC")) {
            roomObs = roomService.getRooms();
        } else {
            roomObs = roomService.getPrivateRooms();
        }

        compositeDisposable.add(roomObs
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initializeAdapter, error -> {
                    Toast.makeText(this, getString(R.string.failed_to_connect), Toast.LENGTH_LONG).show();
                    Log.e("RoomOverviewActivity", error.getMessage());
                }));
    }

    /**
     * Initializes the rooms adapter to show all the rooms that
     * were retrieved from the game-service back-end.
     *
     * @param rooms The rooms that need to be used by the adapter.
     */
    private void initializeAdapter(List<Room> rooms) {
        compositeDisposable.add(userService.getUser("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(myself -> {
                    progressBar.setVisibility(View.GONE);
                    RoomRecyclerAdapter roomAdapter = new RoomRecyclerAdapter(this, rooms, myself);
                    lvRoom.setAdapter(roomAdapter);
                    lvRoom.setLayoutManager(new LinearLayoutManager(this));
                }));
    }
}
