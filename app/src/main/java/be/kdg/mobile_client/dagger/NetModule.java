package be.kdg.mobile_client.dagger;

import javax.inject.Singleton;

import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is similar to the @configuration class in Spring.
 */
public class NetModule {

    public static final String BASE_URL = "https://poker-user-service.herokuapp.com";

//    @Provides
//    @Singleton
//    Retrofit provideRetrofit() {
//        return new Retrofit
//                .Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(BASE_URL)
//                .build();
//    }
//
//    @Provides
//    UserService provideUserService(Retrofit retrofit) {
//        return retrofit.create(UserService.class);
//    }

    SharedPrefService provideSharedPrefService() {
        return new SharedPrefService();
    }
}