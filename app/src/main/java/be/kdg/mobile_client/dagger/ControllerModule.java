package be.kdg.mobile_client.dagger;

import android.content.Context;

import javax.inject.Singleton;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import be.kdg.mobile_client.services.SharedPrefService;
import dagger.Module;
import dagger.Provides;

/**
 * Comparable to Configuration class in Spring.
 * All Services that are needed are provided.
 */
@Module
public class ControllerModule {

    private final FragmentActivity mActivity;
    private final SharedPrefService sharedPrefService;

    public ControllerModule(FragmentActivity activity) {
        mActivity = activity;
        sharedPrefService = new SharedPrefService();
    }

    @Provides
    Context context() {
        return mActivity;
    }

    @Provides
    FragmentActivity activity() {
        return mActivity;
    }

    @Provides
    FragmentManager fragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    @Provides
    SharedPrefService sharedPrefService() {
        return sharedPrefService;
    }

}