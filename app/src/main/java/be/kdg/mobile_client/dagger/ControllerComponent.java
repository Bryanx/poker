package be.kdg.mobile_client.dagger;

import javax.inject.Singleton;

import be.kdg.mobile_client.activities.LoginActivity;
import be.kdg.mobile_client.activities.MainActivity;
import be.kdg.mobile_client.activities.MenuActivity;
import be.kdg.mobile_client.activities.RoomActivity;
import dagger.Subcomponent;

@Singleton
@Subcomponent(modules = {ControllerModule.class})
public interface ControllerComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(MenuActivity menuActivity);
    void inject(RoomActivity menuActivity);
}
