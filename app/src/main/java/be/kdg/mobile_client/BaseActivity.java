package be.kdg.mobile_client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.shared.di.components.ControllerComponent;
import be.kdg.mobile_client.shared.di.modules.ControllerModule;
import be.kdg.mobile_client.user.UserViewModel;
import be.kdg.mobile_client.user.model.User;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Overrides a standard activity and provides the controller component to its children.
 *
 * @see be.kdg.mobile_client.App
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    @Named("UserViewModel")
    ViewModelProvider.Factory factory;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * Initializes the fire base app and subscribes to the richt
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        UserViewModel viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        viewModel.getUser("").observe(this, user -> {
            Log.d("BaseActivity", "onCreate: observing user " + user.getUsername());
            FirebaseMessaging.getInstance().subscribeToTopic(user.getId());
        });
    }

    /**
     * Navigate to a different activity
     *
     * @param destination activity
     */
    protected void navigateTo(Class<? extends AppCompatActivity> destination) {
        Intent intent = new Intent(this, destination);
        this.startActivity(intent);
    }

    /**
     * Navigate to a different activity with extra
     *
     * @param destination activity
     */
    protected void navigateTo(Class<? extends AppCompatActivity> destination, String extraTag, String extra) {
        Intent intent = new Intent(this, destination);
        intent.putExtra(extraTag, extra);
        this.startActivity(intent);
    }

    /**
     * All activities that require login should call this method
     *
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
        return ((App) getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(this));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
