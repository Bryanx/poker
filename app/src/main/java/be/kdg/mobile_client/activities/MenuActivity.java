package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
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
    @BindView(R.id.ivLogo) ImageView ivLogo;
    @Inject SharedPrefService sharedPrefService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
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
        btnJoinGame.setOnClickListener(e -> navigateTo(OverviewActivity.class));
        btnFriends.setOnClickListener(e -> navigateTo(FriendsActivity.class));
        btnRankings.setOnClickListener(e -> navigateTo(RankingsActivity.class));
        btnLogout.setOnClickListener(e -> {
            sharedPrefService.saveToken(this, null); // remove token
            navigateTo(MainActivity.class);
        });
    }
}
