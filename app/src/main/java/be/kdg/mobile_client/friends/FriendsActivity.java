package be.kdg.mobile_client.friends;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.user.model.User;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.user.search.UserSearchActivity;
import be.kdg.mobile_client.user.UserViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for displaying friends of the current user.
 */
public class FriendsActivity extends BaseActivity {
    @BindView(R.id.lvFriends) RecyclerView lvFriends;
    @BindView(R.id.btnSearch) Button btnSearch;
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tvNoBros) TextView tvNoBros;
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
        btnSearch.setOnClickListener(e -> navigateTo(UserSearchActivity.class));
        btnBack.setOnClickListener(e -> navigateTo(MenuActivity.class));
        viewModel.getMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
        progressBar.setVisibility(View.GONE);
        if (friends.size() == 0) tvNoBros.setVisibility(View.VISIBLE);
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
