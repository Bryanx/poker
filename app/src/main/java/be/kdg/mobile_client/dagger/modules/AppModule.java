package be.kdg.mobile_client.dagger.modules;

import android.app.Application;

import javax.inject.Singleton;

import be.kdg.mobile_client.services.SharedPrefService;
import dagger.Module;
import dagger.Provides;
import lombok.RequiredArgsConstructor;

/**
 * Bootstrapper module for providing services to activities.
 * All methods declared in this module are global scope.
 */
@RequiredArgsConstructor
@Module
public class AppModule {

    private final Application mApplication;

    @Provides
    @Singleton
    public Application application() {
        return mApplication;
    }

}