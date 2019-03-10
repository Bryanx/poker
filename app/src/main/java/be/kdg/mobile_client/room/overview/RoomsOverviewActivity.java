package be.kdg.mobile_client.room.overview;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.room.model.Room;
import be.kdg.mobile_client.room.RoomService;
import be.kdg.mobile_client.room.viewmodel.OverviewViewModel;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.user.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Activity that can show a list of public rooms, private rooms or rooms that need to
 * be edited.
 */
public class RoomsOverviewActivity extends BaseActivity {
    @BindView(R.id.tvOverviewHeader) TextView tvOverviewHeader;
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.lvUser) RecyclerView lvRoom;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tvNoRooms) TextView tvNoRooms;
    @Inject OverviewViewModel viewModel;
    @Inject UserService userService;
    @Inject RoomService roomService;
    @Inject SharedPrefService sharedPrefService;
    private Button btnEdit;
    private RoomRecyclerAdapter roomAdapter;
    private boolean publicRooms;
    private boolean editMode = false;

    /**
     * The string resource that was passed to this activity will determine which data
     * needs to be loaded into the adapter.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getControllerComponent().inject(this);

        publicRooms = getIntent().getStringExtra("type").equalsIgnoreCase("PUBLIC");
        if (publicRooms) setContentView(R.layout.activity_overview_public);
        else setContentView(R.layout.activity_overview_private);

        if (!publicRooms) btnEdit = findViewById(R.id.btnEdit);

        ButterKnife.bind(this);
        addEventHandlers();
        getRooms();
    }

    /**
     * Only adds the second handler if we are in a private room overview.
     */
    private void addEventHandlers() {
        btnBack.setOnClickListener(e -> navigateTo(MenuActivity.class));

        if (!publicRooms) {
            btnEdit.setOnClickListener(e -> {
                editMode = !editMode;
                getRooms();
            });
        }
    }

    /**
     * Gets all the rooms that are needed based on the type of rooms that need to be
     * displayed.
     * <p>
     * If the rooms are loaded, that the progress bar will disappear
     */
    public void getRooms() {
        progressBar.setVisibility(View.VISIBLE);
        String header;

        if (editMode) {
            viewModel.getPrivateRoomsOwner().observe(this, this::initializeAdapter);
            btnEdit.setText(R.string.done);
            header = getString(R.string.editRooms).toUpperCase();
        } else if (!publicRooms) {
            viewModel.getPrivateRooms().observe(this, this::initializeAdapter);
            btnEdit.setText(R.string.edit);
            header = getString(R.string.privateRooms).toUpperCase();
        } else {
            viewModel.getPublicRooms().observe(this, this::initializeAdapter);
            header = getString(R.string.publicRooms).toUpperCase();
        }

        tvOverviewHeader.setText(header);
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
                    if (rooms.size() == 0) tvNoRooms.setVisibility(View.VISIBLE);
                    roomAdapter = new RoomRecyclerAdapter(this, rooms, myself, roomService, editMode);
                    lvRoom.setAdapter(roomAdapter);
                    lvRoom.setLayoutManager(new LinearLayoutManager(this));
                }));
    }

    /**
     * If the activity starts again than we need to check if the user is still authorized.
     */
    @Override
    protected void onResume() {
        checkIfAuthorized(sharedPrefService);
        if (roomAdapter != null) roomAdapter.notifyDataSetChanged(); // refresh rooms
        super.onResume();
    }
}
