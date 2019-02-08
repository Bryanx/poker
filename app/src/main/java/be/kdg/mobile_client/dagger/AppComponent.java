package be.kdg.mobile_client.dagger;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(App app);

    //Controller component can be obtained
    ControllerComponent newControllerComponent(ControllerModule controllerModule);
}
