package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.FriendAdapter;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.services.CallbackWrapper;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for displaying friends of the current user.
 */
public class FriendsActivity extends BaseActivity {
    @BindView(R.id.lvFriends) ListView lvFriends;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.btnBack) Button btnBack;
    @Inject SharedPrefService sharedPrefService;
    @Inject UserService userService;
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        addEventListners();

        friendAdapter = new FriendAdapter(this);
        lvFriends.setAdapter(friendAdapter);
        loadFriends();
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

    private void loadFriends() {
        userService.getMySelf().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                for (User friend : response.body().getFriends()) {
                    friendAdapter.add(friend);
                }
            } else {
                Toast.makeText(this, "Error loading friends", Toast.LENGTH_LONG).show();
            }
        }));
    }
}
