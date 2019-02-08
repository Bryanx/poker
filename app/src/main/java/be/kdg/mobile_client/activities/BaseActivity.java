package be.kdg.mobile_client.activities;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.dagger.ControllerComponent;
import be.kdg.mobile_client.dagger.ControllerModule;

/**
 * Overrides a standard activity and provides the controller component to its children.
 * @see be.kdg.mobile_client.App
 */
public class BaseActivity extends AppCompatActivity {

    @UiThread
    protected ControllerComponent getControllerComponent() {
        return ((App)getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(this));
    }
}
