package be.kdg.mobile_client;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.mobile_client.friends.FriendsActivity;
import be.kdg.mobile_client.room.overview.RoomOverviewActivity;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.user.settings.UserSettingsActivity;
import be.kdg.mobile_client.user.rankings.RankingsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The main menu of the app.
 */
public class MenuActivity extends BaseActivity {
    @BindView(R.id.btnLogout) Button btnLogout;
    @BindView(R.id.btnPublicGame) Button btnPublicGame;
    @BindView(R.id.btnPrivateGame) Button btnPrivateGame;
    @BindView(R.id.btnFriends) Button btnFriends;
    @BindView(R.id.btnRankings) Button btnRankings;
    @BindView(R.id.btnSettings) Button btnSettings;
    @BindView(R.id.ivLogo) ImageView ivLogo;
    @Inject SharedPrefService sharedPrefService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        addEventHandlers();
        loadImages();
    }

    private void loadImages() {
        Picasso.get()
                .load(R.drawable.logo_white)
                .resize(700, 400)
                .centerInside()
                .into(ivLogo);
    }

    private void addEventHandlers() {
        btnPublicGame.setOnClickListener(e -> navigateTo(RoomOverviewActivity.class, "TYPE", "PUBLIC"));
        btnPrivateGame.setOnClickListener(e -> navigateTo(RoomOverviewActivity.class, "TYPE", "PRIVATE"));
        btnFriends.setOnClickListener(e -> navigateTo(FriendsActivity.class));
        btnRankings.setOnClickListener(e -> navigateTo(RankingsActivity.class));
        btnSettings.setOnClickListener(e -> navigateTo(UserSettingsActivity.class));
        btnLogout.setOnClickListener(e -> {
            sharedPrefService.saveToken(this, null); // remove token
            navigateTo(MainActivity.class);
        });
    }

    @Override
    protected void onResume() {
        checkIfAuthorized(sharedPrefService);
        super.onResume();
    }
}
