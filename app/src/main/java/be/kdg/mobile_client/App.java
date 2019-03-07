package be.kdg.mobile_client;

import android.app.Application;

import androidx.annotation.UiThread;
import be.kdg.mobile_client.shared.di.components.AppComponent;
import be.kdg.mobile_client.shared.di.modules.AppModule;
import be.kdg.mobile_client.shared.di.components.DaggerAppComponent;

/**
 * Overrides the default Application file.
 * Gives access to the app component on all the activies
 * The app component will provide all the dependencies.
 */
public class App extends Application {
    private AppComponent appComponent;

    @UiThread
    public AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this)).build();
        }
        return appComponent;
    }
}