package be.kdg.mobile_client.dagger;

import be.kdg.mobile_client.activities.LoginActivity;
import be.kdg.mobile_client.activities.MainActivity;
import be.kdg.mobile_client.activities.MenuActivity;
import dagger.Subcomponent;

@Subcomponent(modules = {ControllerModule.class})
public interface ControllerComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(MenuActivity menuActivity);
}
