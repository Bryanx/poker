package be.kdg.mobile_client.shared.di.modules;

import android.app.Application;

import javax.inject.Singleton;

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