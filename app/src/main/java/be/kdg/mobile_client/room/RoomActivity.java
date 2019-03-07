package be.kdg.mobile_client.room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.databinding.ActivityRoomBinding;
import be.kdg.mobile_client.chat.ChatFragment;
import be.kdg.mobile_client.chat.ChatService;
import be.kdg.mobile_client.shared.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity of an individual room.
 * This room contains a fragment in which the chat messages are processed.
 */
public class RoomActivity extends BaseActivity {
    @BindView(R.id.btnShowChat) Button btnShowChat;
    @Inject FragmentManager fragmentManager;
    @Inject SharedPrefService sharedPrefService;
    @Inject ChatService chatService;
    @Inject @Named("RoomViewModel") ViewModelProvider.Factory factory;
    private ChatFragment chatFragment;
    private RoomViewModel viewModel;
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        roomId = getIntent().getIntExtra(getString(R.string.room_id), 0);
        setUpViewModel();
        ButterKnife.bind(this);
        setUpChatFragment();
        addEventHandlers();
    }

    private void setUpViewModel() {
        viewModel = ViewModelProviders.of(this,factory).get(RoomViewModel.class);
        ActivityRoomBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        viewModel.init(roomId);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
    }

    private void setUpChatFragment() {
        chatFragment = (ChatFragment) fragmentManager.findFragmentByTag(getString(R.string.chat_fragment_tag));
        chatFragment.connectChat(roomId, chatService, sharedPrefService.getToken(this).getUsername());
        newTransaction().hide(chatFragment).commit(); // initially hide the chatfragment
    }

    private void addEventHandlers() {
        btnShowChat.setOnClickListener(e -> {
            if (chatFragment != null && chatFragment.isHidden()) {
                newTransaction().show(chatFragment).commit();
            } else {
                newTransaction().hide(chatFragment).commit();
            }
        });
        viewModel.getToast().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Create a fragment transaction with sliding animation
     */
    private FragmentTransaction newTransaction() {
        return fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
    }

    @Override
    protected void onDestroy() {
        viewModel.leaveRoom();
        chatFragment.leaveChat();
        super.onDestroy();
    }
}
