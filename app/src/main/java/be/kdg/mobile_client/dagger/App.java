package be.kdg.mobile_client.dagger;

import android.app.Application;

import androidx.annotation.UiThread;

/**
 * Overrides the default Application file.
 * Every Activity will now have access to the components - modules - services.
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