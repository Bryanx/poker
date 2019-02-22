package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.FriendRecyclerAdapter;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.shared.CallbackWrapper;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for displaying friends of the current user.
 */
public class FriendsActivity extends BaseActivity {
    @BindView(R.id.lvFriends) RecyclerView lvFriends;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.btnBack) Button btnBack;
    @Inject SharedPrefService sharedPrefService;
    @Inject UserService userService;
    private User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        addEventListners();
        getFriends();
    }

    private void addEventListners() {
        btnSearch.setOnClickListener(e -> {
            Intent intent = new Intent(this, UserSearchActivity.class);
            startActivity(intent);
        });
        btnBack.setOnClickListener(e -> {
            Intent intent = new Intent(this,  MenuActivity.class);
            startActivity(intent);
        });
    }

    private void getFriends() {
        userService.getMySelf().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                myself = response.body();
                initializeAdapter(response.body().getFriends());
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.error_message), Toast.LENGTH_LONG).show();
            }
        }));
    }

    /**
     * Initializes the friend adapter to show all the friends that
     * were retrieved from the user-service back-end.
     *
     * @param friends The friends that need to be used by the adapter.
     */
    private void initializeAdapter(List<User> friends) {
        FriendRecyclerAdapter friendAdapter = new FriendRecyclerAdapter(this, friends);
        lvFriends.setAdapter(friendAdapter);
        lvFriends.setLayoutManager(new LinearLayoutManager(this));
    }

    public void removeFriend(User friend) {
        myself.getFriends().remove(friend);
        userService.changeUser(myself).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response != null && response.isSuccessful()) {
                Toast.makeText(getApplicationContext(), friend.getUsername() + " " +
                        getString(R.string.was_removed_as_a_friend), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_updating_friends), Toast.LENGTH_LONG).show();
            }
        }));
    }
}
