package be.kdg.mobile_client.activities;

import android.content.Intent;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.dagger.ControllerComponent;
import be.kdg.mobile_client.dagger.ControllerModule;
import be.kdg.mobile_client.services.SharedPrefService;

/**
 * Overrides a standard activity and provides the controller component to its children.
 * @see be.kdg.mobile_client.App
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * All activities that require login should call this method
     * @param sharedPrefService injeted sharedprefservice fo the activity
     */
    @UiThread
    protected void checkIfAuthorized(SharedPrefService sharedPrefService) {
        if (!sharedPrefService.hasToken(this)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Retrieve ControllerComponent so services become injectable.
     * @return
     */
    @UiThread
    protected ControllerComponent getControllerComponent() {
        return ((App)getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(this));
    }
}
