package be.kdg.mobile_client;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.databinding.ActivityMenuBinding;
import be.kdg.mobile_client.friends.FriendsActivity;
import be.kdg.mobile_client.room.overview.RoomsOverviewActivity;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.user.UserActivity;
import be.kdg.mobile_client.user.UserViewModel;
import be.kdg.mobile_client.user.model.User;
import be.kdg.mobile_client.user.rankings.RankingsActivity;
import be.kdg.mobile_client.user.settings.UserSettingsActivity;

/**
 * The main menu of the app.
 */
public class MenuActivity extends BaseActivity {
    @Inject SharedPrefService sharedPrefService;
    @Inject @Named("UserViewModel") ViewModelProvider.Factory factory;
    private UserViewModel viewModel;
    private User myself;
    private ActivityMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        addEventHandlers();
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
            binding.tvUserLevel.setText(String.valueOf(user.getLevel()));
            binding.progressBarLevel.setMax(user.getThresholdTillNextLevel());
            binding.progressBarLevel.setProgress(user.getXpTillNext());
            binding.tvCoins.setText(String.valueOf(user.getChips()));
        });
    }

    /**
     * Loads the images that need to be shown on the menu.
     */
    private void loadImages() {
        placeImage(R.drawable.logo_white, binding.ivLogo, 700, 400);
        placeImage(R.drawable.bell, binding.ivBell, 35, 35);
        placeImage(R.drawable.coins, binding.ivCoins, 35, 35);
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
        binding.btnPublicGame.setOnClickListener(e -> navigateTo(RoomsOverviewActivity.class, "type", "PUBLIC"));
        binding.btnPrivateGame.setOnClickListener(e -> navigateTo(RoomsOverviewActivity.class, "type", "PRIVATE"));
        binding.btnFriends.setOnClickListener(e -> navigateTo(FriendsActivity.class));
        binding.btnRankings.setOnClickListener(e -> navigateTo(RankingsActivity.class));
        binding.ivSettings.setOnClickListener(e -> navigateTo(UserSettingsActivity.class));
        binding.btnAccount.setOnClickListener(e -> navigateTo(UserActivity.class, getString(R.string.userid), ""));
        binding.btnLogout.setOnClickListener(e -> {
            if (myself != null ) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(myself.getId());
            }
            sharedPrefService.saveToken(this, null); // remove token
            navigateTo(MainActivity.class);
        });
        binding.ivBell.setOnClickListener(e -> binding.drawerLayout.openDrawer(GravityCompat.START));
    }

    @Override
    protected void onResume() {
        checkIfAuthorized(sharedPrefService);
        super.onResume();
    }
}
