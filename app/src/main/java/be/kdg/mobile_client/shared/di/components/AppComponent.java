package be.kdg.mobile_client.shared.di.components;

import javax.inject.Singleton;

import be.kdg.mobile_client.App;
import be.kdg.mobile_client.shared.di.modules.AppModule;
import be.kdg.mobile_client.shared.di.modules.ControllerModule;
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
