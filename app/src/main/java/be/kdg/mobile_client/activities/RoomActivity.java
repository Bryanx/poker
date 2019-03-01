package be.kdg.mobile_client.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.databinding.ActivityRoomBinding;
import be.kdg.mobile_client.fragments.ChatFragment;
import be.kdg.mobile_client.services.ChatService;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.viewmodels.RoomViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity of an individual room.
 * This room contains a fragment in which the chat messages are processed.
 */
public class RoomActivity extends BaseActivity {
    @BindView(R.id.btnShowChat) Button btnShowChat;
    @BindView(R.id.llFragment) LinearLayout llFragment;
    @Inject FragmentManager fragmentManager;
    @Inject SharedPrefService sharedPrefService;
    @Inject ChatService chatService;
    @Inject @Named("RoomViewModel") ViewModelProvider.Factory factory;
    private ChatFragment chatFragment;
    private RoomViewModel viewModel;
    private int roomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        roomNumber = getIntent().getIntExtra(getString(R.string.room_id), 0);
        viewModel = ViewModelProviders.of(this,factory).get(RoomViewModel.class);
        ActivityRoomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        viewModel.init(roomNumber, binding);
        binding.setLifecycleOwner(this);
        ButterKnife.bind(this);
        initialiseViews();
        handleShowChatButton();
    }

    private void initialiseViews() {
        chatFragment = (ChatFragment) fragmentManager.findFragmentByTag(getString(R.string.chat_fragment_tag));
        chatFragment.connectChat(roomNumber, chatService, sharedPrefService.getToken(this).getUsername());
        hideFragment(chatFragment); // initially hide the chatfragment
    }

    private void handleShowChatButton() {
        btnShowChat.setOnClickListener(e -> {
            if (chatFragment != null && chatFragment.isHidden()) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                        .show(chatFragment)
                        .commit();
            } else {
                hideFragment(chatFragment);
            }
        });
    }

    private void hideFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                .hide(fragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        viewModel.leaveRoom();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        viewModel.leaveRoom();
        chatFragment.leaveChat();
        super.onBackPressed();
    }
}
