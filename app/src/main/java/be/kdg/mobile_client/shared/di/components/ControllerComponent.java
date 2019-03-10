package be.kdg.mobile_client.shared.di.components;

import javax.inject.Singleton;

import be.kdg.mobile_client.MainActivity;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.chat.ChatFragment;
import be.kdg.mobile_client.chat.ChatService;
import be.kdg.mobile_client.friends.FriendsActivity;
import be.kdg.mobile_client.room.RoomActivity;
import be.kdg.mobile_client.room.RoomService;
import be.kdg.mobile_client.room.RoomViewModel;
import be.kdg.mobile_client.room.overview.PrivateRoomOverviewActivity;
import be.kdg.mobile_client.room.overview.PublicRoomOverviewActivity;
import be.kdg.mobile_client.room.overview.RoomOverviewActivity;
import be.kdg.mobile_client.shared.WebSocketService;
import be.kdg.mobile_client.shared.di.modules.ControllerModule;
import be.kdg.mobile_client.user.UserActivity;
import be.kdg.mobile_client.user.UserViewModel;
import be.kdg.mobile_client.user.authorization.LoginActivity;
import be.kdg.mobile_client.user.authorization.RegisterActivity;
import be.kdg.mobile_client.user.rankings.RankingsActivity;
import be.kdg.mobile_client.user.search.UserSearchActivity;
import be.kdg.mobile_client.user.settings.UserSettingsActivity;
import dagger.Subcomponent;
import ua.naiksoftware.stomp.StompClient;

/**
 * Overview of where the ControllerModule should be injected.
 */
@Singleton
@Subcomponent(modules = {ControllerModule.class})
public interface ControllerComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(MenuActivity menuActivity);
    void inject(RoomActivity roomActivity);
    void inject(ChatFragment chatFragment);
    void inject(RegisterActivity registerActivity);
    void inject(FriendsActivity friendsActivity);
    void inject(RoomOverviewActivity roomOverViewActivity);
    void inject(RankingsActivity rankingsActivity);
    void inject(UserActivity userActivity);
    void inject(UserSearchActivity userSearchActivity);
    void inject(PrivateRoomOverviewActivity privateRoomOverviewActivity);
    void inject(PublicRoomOverviewActivity publicRoomOverviewActivity);
    void inject(UserViewModel userViewModel);
    void inject(StompClient stompClient);
    void inject(UserSettingsActivity userSettingsActivity);
    void inject(RoomService roomService);
    WebSocketService websocketService();
    ChatService chatService();
    RoomViewModel roomViewModel();
}
