package be.kdg.mobile_client.dagger.components;

import javax.inject.Singleton;

import be.kdg.mobile_client.activities.AccountActivity;
import be.kdg.mobile_client.activities.FriendsActivity;
import be.kdg.mobile_client.activities.LoginActivity;
import be.kdg.mobile_client.activities.MainActivity;
import be.kdg.mobile_client.activities.MenuActivity;
import be.kdg.mobile_client.activities.OverviewActivity;
import be.kdg.mobile_client.activities.RankingsActivity;
import be.kdg.mobile_client.activities.RegisterActivity;
import be.kdg.mobile_client.activities.RoomActivity;
import be.kdg.mobile_client.activities.UserSearchActivity;
import be.kdg.mobile_client.dagger.modules.ControllerModule;
import be.kdg.mobile_client.fragments.ChatFragment;
import be.kdg.mobile_client.viewmodels.RankingsViewModel;
import dagger.Subcomponent;

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
    void inject(OverviewActivity overviewActivity);
    void inject(RankingsActivity rankingsActivity);
    void inject(AccountActivity accountActivity);
    void inject(UserSearchActivity userSearchActivity);
    void inject(RankingsViewModel rankingsViewModel);
}
