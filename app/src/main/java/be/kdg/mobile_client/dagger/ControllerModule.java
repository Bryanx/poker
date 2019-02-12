package be.kdg.mobile_client.dagger;

import android.content.Context;

import com.google.gson.Gson;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import be.kdg.mobile_client.services.ChatService;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Comparable to @Configuration class in Spring.
 * All Services that are needed are provided.
 * These services are accesible with @Inject.
 */
@Module
public class ControllerModule {
    public static final String API_BASE_URL = "https://poker-user-service.herokuapp.com";
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

    @Provides
    GsonConverterFactory gsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    Gson gson() {
        return new Gson();
    }

    @Provides
    Retrofit retrofit() {
        return new Retrofit
                .Builder()
                .addConverterFactory(gsonConverter())
                .baseUrl(API_BASE_URL)
                .build();
    }

    @Provides
    UserService userService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    ChatService stompService() {
        return new ChatService();
    }
}