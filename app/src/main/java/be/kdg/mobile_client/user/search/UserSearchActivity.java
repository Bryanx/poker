package be.kdg.mobile_client.user.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.friends.Friend;
import be.kdg.mobile_client.friends.FriendsActivity;
import be.kdg.mobile_client.notification.NotificationViewModel;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.user.UserViewModel;
import be.kdg.mobile_client.user.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSearchActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.lvUser) RecyclerView lvUser;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @Inject @Named("UserViewModel") ViewModelProvider.Factory factory;
    @Inject SharedPrefService sharedPrefService;
    @Inject NotificationViewModel notificationViewModel;
    private UserViewModel userViewModel;
    private User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getControllerComponent().inject(this);
        setContentView(R.layout.activity_usersearch);
        ButterKnife.bind(this);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        addEventHandlers();
        userViewModel.getUser("").observe(this, me -> myself = me);
    }

    /**
     * Adds event handlers to this activity.
     */
    private void addEventHandlers() {
        btnBack.setOnClickListener(e -> navigateTo(FriendsActivity.class));
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed()) {
                    getUsersByName(etSearch.getText().toString());
                }
            }
            return false;
        });
    }

    /**
     * Gets all the users that have the searched string inside their name
     * and puts them into a list adapter.
     *
     * @param name The search string.
     */
    private void getUsersByName(String name) {
        progressBar.setVisibility(View.VISIBLE);
        userViewModel.getUsers(name).observe(this, this::initializeAdapter);
    }

    /**
     * Initializes the rooms adapter to show all the rooms that
     * were retrieved from the game-service back-end.
     *
     * @param users The users that need to be used by the adapter.
     */
    private void initializeAdapter(List<User> users) {
        progressBar.setVisibility(View.GONE);
        UserRecyclerAdapter userAdapter = new UserRecyclerAdapter(this, users, myself, notificationViewModel);
        lvUser.setAdapter(userAdapter);
        lvUser.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * Adds a friend to the current user
     *
     * @param friend friend to be added
     */
    public void addFriend(User friend) {
        myself.addFriend(new Friend(friend.getId()));
        userViewModel.changeUser(myself);
    }
}
