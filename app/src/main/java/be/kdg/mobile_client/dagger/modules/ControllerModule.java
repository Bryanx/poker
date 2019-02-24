package be.kdg.mobile_client.dagger.modules;

import android.content.Context;

import com.google.gson.Gson;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.services.ChatService;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.shared.EmailValidator;
import be.kdg.mobile_client.shared.UsernameValidator;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Comparable to @Configuration class in Spring.
 * All Services that are needed are provided.
 * These services are accessible with @Inject.
 */
@Module
public class ControllerModule {
    private static final String API_BASE_URL_USER = "https://poker-user-service.herokuapp.com";
    private static final String API_BASE_URL_GAME = "https://poker-game-service.herokuapp.com";
    //private static final String API_BASE_URL_USER = "http://localhost:5000";
    //private static final String API_BASE_URL_GAME = "http://localhost:5001";
    private final FragmentActivity mActivity;
    private final SharedPrefService sharedPrefService;

    public ControllerModule(FragmentActivity activity) {
        mActivity = activity;
        sharedPrefService = new SharedPrefService();
    }

    @Provides
    public Context context() {
        return mActivity;
    }

    @Provides
    public FragmentActivity activity() {
        return mActivity;
    }

    @Provides
    public FragmentManager fragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    @Provides
    public SharedPrefService sharedPrefService() {
        return sharedPrefService;
    }

    @Provides
    public GsonConverterFactory gsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    public Gson gson() {
        return new Gson();
    }

    @Provides
    public EmailValidator emailValidator() { return new EmailValidator(); }

    @Provides
    public UsernameValidator usernameValidator() { return new UsernameValidator(); }

    @Provides
    public OkHttpClient okHttpClient(SharedPrefService sharedPrefService) {
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
    public UserService userService(OkHttpClient client) {
        return new Retrofit
                .Builder()
                .client(client)
                .addConverterFactory(gsonConverter())
                .baseUrl(API_BASE_URL_USER)
                .build()
                .create(UserService.class);
    }

    @Provides
    public GameService gameService(OkHttpClient client) {
        return new Retrofit
                .Builder()
                .client(client)
                .addConverterFactory(gsonConverter())
                .baseUrl(API_BASE_URL_GAME)
                .build()
                .create(GameService.class);
    }

    @Provides
    public ChatService stompService() {
        return new ChatService();
    }
}