package be.kdg.mobile_client.room;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.chat.ChatFragment;
import be.kdg.mobile_client.chat.ChatService;
import be.kdg.mobile_client.chat.ChatViewModel;
import be.kdg.mobile_client.databinding.ActivityRoomBinding;
import be.kdg.mobile_client.room.overview.RoomsOverviewActivity;
import be.kdg.mobile_client.shared.SharedPrefService;

/**
 * Main activity of an individual room.
 * This room contains a fragment in which the chat messages are processed.
 */
public class RoomActivity extends BaseActivity {
    @Inject FragmentManager fragmentManager;
    @Inject SharedPrefService sharedPrefService;
    @Inject ChatService chatService;
    @Inject @Named("RoomViewModel") ViewModelProvider.Factory factory;
    @Inject @Named("ChatViewModel") ViewModelProvider.Factory chatFactory;
    private ChatFragment chatFragment;
    private RoomViewModel viewModel;
    private ChatViewModel chatViewModel;
    private int roomId;
    private ActivityRoomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        roomId = getIntent().getIntExtra(getString(R.string.room_id), 0);
        setUpViewModel();
        setUpChatFragment();
        addEventHandlers();
    }

    private void setUpViewModel() {
        viewModel = ViewModelProviders.of(this,factory).get(RoomViewModel.class);
        chatViewModel = ViewModelProviders.of(this,chatFactory).get(ChatViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room);
        viewModel.init(roomId);
        binding.setViewmodel(viewModel);
        binding.toolBarRight.setChatViewModel(chatViewModel);
        binding.setLifecycleOwner(this);
    }

    private void setUpChatFragment() {
        chatFragment = (ChatFragment) fragmentManager.findFragmentByTag(getString(R.string.chat_fragment_tag));
        chatFragment.setViewModel(chatViewModel);
        chatFragment.connectChat(roomId, chatService, sharedPrefService.getToken(this).getUsername());
    }

    private void addEventHandlers() {
        binding.toolBarRight.btnShowChat.setOnClickListener(e -> {
            binding.drawerLayout.openDrawer(GravityCompat.START);
            chatViewModel.getUnreadMessages().setValue(0);
        });
        binding.toolBarLeft.btnLeave.setOnClickListener(e -> {
            exit();
            navigateTo(RoomsOverviewActivity.class,"type", "PUBLIC");
        });
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset != 0) chatViewModel.getUnreadMessages().setValue(0);
            }
        });
        viewModel.getToast().observe(this, message -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onResume() {
        checkIfAuthorized(sharedPrefService);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        exit();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        exit();
        super.onStop();
    }

    private void exit() {
        chatFragment.leaveChat();
        viewModel.leaveRoom();
    }
}
