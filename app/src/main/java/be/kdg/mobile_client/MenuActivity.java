package be.kdg.mobile_client;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.friends.FriendsActivity;
import be.kdg.mobile_client.room.overview.RoomsOverviewActivity;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.notification.NotificationFragment;
import be.kdg.mobile_client.user.UserActivity;
import be.kdg.mobile_client.user.UserViewModel;
import be.kdg.mobile_client.user.model.User;
import be.kdg.mobile_client.user.rankings.RankingsActivity;
import be.kdg.mobile_client.user.settings.UserSettingsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The main menu of the app.
 */
public class MenuActivity extends BaseActivity {
    @BindView(R.id.tvUserLevel) TextView tvUserLevel;
    @BindView(R.id.tvCoins) TextView tvCoins;
    @BindView(R.id.btnLogout) Button btnLogout;
    @BindView(R.id.btnPublicGame) Button btnPublicGame;
    @BindView(R.id.btnPrivateGame) Button btnPrivateGame;
    @BindView(R.id.btnFriends) Button btnFriends;
    @BindView(R.id.btnRankings) Button btnRankings;
    @BindView(R.id.btnAccount) Button btnSettings;
    @BindView(R.id.ivLogo) ImageView ivLogo;
    @BindView(R.id.ivBell) ImageView ivBell;
    @BindView(R.id.ivCoins) ImageView ivCoins;
    @BindView(R.id.ivSettings) ImageView ivSettings;
    @BindView(R.id.progressBarLevel) ProgressBar progressBarLevel;

    @Inject SharedPrefService sharedPrefService;
    @Inject FragmentManager fragmentManager;
    @Inject @Named("UserViewModel") ViewModelProvider.Factory factory;
    private NotificationFragment notificationFragment;
    private UserViewModel viewModel;
    private User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        addEventHandlers();
        setUpNotificationFragment();
        loadImages();
        getUserInfo();
    }

    /**
     * Gets all the info that a user needs like the current level, the progress to the next level
     * and the number of chips.
     */
    private void getUserInfo() {
        viewModel.getUser("").observe(this, user -> {
            this.myself = user;
            tvUserLevel.setText(String.valueOf(user.getLevel()));
            progressBarLevel.setMax(user.getThresholdTillNextLevel());
            progressBarLevel.setProgress(user.getXpTillNext());
            tvCoins.setText(String.valueOf(user.getChips()));
        });
    }

    /**
     * Sets up the notification fragment that will be present in this activity.
     */
    private void setUpNotificationFragment() {
        notificationFragment = (NotificationFragment) fragmentManager.findFragmentByTag(getString(R.string.not_fragment_tag));
        newTransaction().hide(notificationFragment).commit();
    }

    /**
     * Create a fragment transaction with sliding animation.
     */
    private FragmentTransaction newTransaction() {
        return fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
    }

    /**
     * Loads the images that need to be shown on the menu.
     */
    private void loadImages() {
        placeImage(R.drawable.logo_white, ivLogo, 700, 400);
        placeImage(R.drawable.bell, ivBell, 35, 35);
        placeImage(R.drawable.coins, ivCoins, 35, 35);
        placeImage(R.drawable.settings, ivSettings, 35, 35);
    }

    /**
     * Places an image inside the image view.
     *
     * @param src    The source of the image.
     * @param target The target where the source needs to be placed.
     */
    private void placeImage(int src, ImageView target, int width, int height) {
        Picasso.get()
                .load(src)
                .resize(width, height)
                .centerInside()
                .into(target);
    }

    private void addEventHandlers() {
        btnPublicGame.setOnClickListener(e -> navigateTo(RoomsOverviewActivity.class, "type", "PUBLIC"));
        btnPrivateGame.setOnClickListener(e -> navigateTo(RoomsOverviewActivity.class, "type", "PRIVATE"));
        btnFriends.setOnClickListener(e -> navigateTo(FriendsActivity.class));
        btnRankings.setOnClickListener(e -> navigateTo(RankingsActivity.class));
        ivSettings.setOnClickListener(e -> navigateTo(UserSettingsActivity.class));
        btnSettings.setOnClickListener(e -> navigateTo(UserActivity.class, "USER_ID", ""));
        btnLogout.setOnClickListener(e -> {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(myself.getId());
            sharedPrefService.saveToken(this, null); // remove token
            navigateTo(MainActivity.class);
        });

        ivBell.setOnClickListener(e -> {
            if (notificationFragment != null && notificationFragment.isHidden()) {
                newTransaction().show(notificationFragment).commit();
            } else {
                newTransaction().hide(notificationFragment).commit();
            }
        });
    }

    @Override
    protected void onResume() {
        checkIfAuthorized(sharedPrefService);
        super.onResume();
    }
}
