package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.adapters.FriendRecyclerAdapter;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.viewmodels.UserViewModel;
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
    @Inject @Named("UserViewModel") ViewModelProvider.Factory factory;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this,factory).get(UserViewModel.class);
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
        viewModel.getMessage().observe(this, message -> {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        });
    }

    private void getFriends() {
        viewModel.getUser("").observe(this, user -> initializeAdapter(user.getFriends()));
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
        viewModel.getUser("").observe(this, me -> {
            me.getFriends().remove(friend);
            viewModel.changeUser(me);
        });
    }
}
