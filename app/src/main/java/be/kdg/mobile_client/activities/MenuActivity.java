package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.activities.BaseActivity;
import be.kdg.mobile_client.activities.MainActivity;
import be.kdg.mobile_client.activities.RoomActivity;
import be.kdg.mobile_client.services.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The main menu of the app.
 */
public class MenuActivity extends BaseActivity {
    @BindView(R.id.btnLogout) Button btnLogout;
    @BindView(R.id.btnJoinGame) Button btnJoinGame;
    @BindView(R.id.btnFriends) Button btnFriends;
    @BindView(R.id.btnRankings) Button btnRankings;
    @Inject SharedPrefService sharedPrefService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        addEventHandlers();
    }

    private void addEventHandlers() {
        btnJoinGame.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), OverviewActivity.class);
            startActivity(intent);
        });
        btnFriends.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
            startActivity(intent);
        });
        btnRankings.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), RankingsActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(e -> {
            sharedPrefService.saveToken(this, null); // remove token
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}
