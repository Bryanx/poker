package be.kdg.mobile_client.dagger;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.services.ChatService;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Comparable to @Configuration class in Spring.
 * All Services that are needed are provided.
 * These services are accesible with @Inject.
 */
@Module
public class ControllerModule {
    //private static final String API_BASE_URL = "https://poker-user-service.herokuapp.com";
    private static final String API_BASE_URL = "http://localhost:5000";
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
    OkHttpClient okHttpClient(SharedPrefService sharedPrefService) {
        Token token = sharedPrefService.getToken(activity());
        if (token == null) return new OkHttpClient();
        return new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token.getAccessToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }

    @Provides
    Retrofit retrofit(OkHttpClient client) {
        return new Retrofit
                .Builder()
                .client(client)
                .addConverterFactory(gsonConverter())
                .baseUrl(API_BASE_URL)
                .build();
    }

    @Provides
    UserService userService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    GameService gameService(Retrofit retrofit) {
        return retrofit.create(GameService.class);
    }

    @Provides
    ChatService stompService() {
        return new ChatService();
    }
}