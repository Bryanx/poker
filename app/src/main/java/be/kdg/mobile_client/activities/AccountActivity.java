package be.kdg.mobile_client.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.shared.CallbackWrapper;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for player accounts. Each player has its own 'page'.
 * The user's data is fetched from the server and loaded in the view components.
 */
public class AccountActivity extends BaseActivity {
    @BindView(R.id.tvHeader) TextView tvHeader;
    @BindView(R.id.tvWins) TextView tvWins;
    @BindView(R.id.tvChips) TextView tvChips;
    @BindView(R.id.tvGames) TextView tvGames;
    @BindView(R.id.tvBestHand) TextView tvBestHand;
    @BindView(R.id.ivPicture) ImageView ivPicture;
    @Inject UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        fetchUser(getIntent().getStringExtra(getString(R.string.userid)));
    }

    private void fetchUser(String id) {
        userService.getUser(id).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                loadUserIntoView(response.body());
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        }));
    }

    private void loadUserIntoView(User user) {
        tvHeader.setText(user.getUsername());
        tvWins.setText(String.valueOf(user.getWins()));
        tvChips.setText(String.valueOf(user.getChips()));
        tvGames.setText(String.valueOf(user.getGamesPlayed()));
        tvBestHand.setText(user.getBestHand());
        loadProfilePicture(user);
    }

    private void loadProfilePicture(User user) {
        if (user.getProfilePicture() != null) {
            byte[] decodedString = Base64.decode(user.getProfilePicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivPicture.setImageBitmap(decodedByte);
        } else {
            Picasso.get().load(R.drawable.user).into(ivPicture); // load default profile picture
        }
    }
}
