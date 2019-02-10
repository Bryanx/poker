package be.kdg.mobile_client.dagger;

import javax.inject.Singleton;

import be.kdg.mobile_client.App;
import dagger.Component;

/**
 * Provides components to applications.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);

    //Controller component can be obtained by injected apps
    ControllerComponent newControllerComponent(ControllerModule controllerModule);
}
