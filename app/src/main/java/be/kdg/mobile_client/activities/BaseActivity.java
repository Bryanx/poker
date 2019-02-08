package be.kdg.mobile_client.activities;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.dagger.ControllerComponent;
import be.kdg.mobile_client.dagger.ControllerModule;

public class BaseActivity extends AppCompatActivity {

    @UiThread
    protected ControllerComponent getControllerComponent() {
        return ((App)getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(this));
    }
}
