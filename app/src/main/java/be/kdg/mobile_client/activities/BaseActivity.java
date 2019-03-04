package be.kdg.mobile_client.activities;

import android.content.Intent;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.dagger.components.ControllerComponent;
import be.kdg.mobile_client.dagger.modules.ControllerModule;
import be.kdg.mobile_client.services.SharedPrefService;

/**
 * Overrides a standard activity and provides the controller component to its children.
 * @see be.kdg.mobile_client.App
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Navigate to a different activity
     * @param destination activity
     */
    protected void navigateTo(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        this.startActivity(intent);
    }

    /**
     * Navigate to a different activity with extra
     * @param destination activity
     */
    protected void navigateTo(Class<?> destination, String extraTag, String extra) {
        Intent intent = new Intent(this, destination);
        intent.putExtra(extraTag, extra);
        this.startActivity(intent);
    }

    /**
     * All activities that require login should call this method
     * @param sharedPrefService injeted sharedprefservice fo the activity
     */
    @UiThread
    protected void checkIfAuthorized(SharedPrefService sharedPrefService) {
        if (!sharedPrefService.hasToken(this)) navigateTo(MainActivity.class);
    }

    /**
     * Retrieve ControllerComponent so services become injectable.
     */
    @UiThread
    protected ControllerComponent getControllerComponent() {
        return ((App)getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(this));
    }
}
