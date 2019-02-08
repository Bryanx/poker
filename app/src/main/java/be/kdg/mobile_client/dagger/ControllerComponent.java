package be.kdg.mobile_client.dagger;

import be.kdg.mobile_client.LoginActivity;
import be.kdg.mobile_client.MainActivity;
import be.kdg.mobile_client.MenuActivity;
import dagger.Subcomponent;

@Subcomponent(modules = {ControllerModule.class})
public interface ControllerComponent {
    void inject(MainActivity mainActivity);
    void inject(LoginActivity loginActivity);
    void inject(MenuActivity menuActivity);
}
