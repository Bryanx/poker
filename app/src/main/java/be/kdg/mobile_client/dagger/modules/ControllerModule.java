package be.kdg.mobile_client.dagger.modules;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.services.ChatService;
import be.kdg.mobile_client.services.GameService;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.services.WebSocketService;
import be.kdg.mobile_client.shared.EmailValidator;
import be.kdg.mobile_client.shared.UsernameValidator;
import be.kdg.mobile_client.shared.Utils;
import be.kdg.mobile_client.shared.ViewModelProviderFactory;
import be.kdg.mobile_client.viewmodels.RoomViewModel;
import be.kdg.mobile_client.viewmodels.UserViewModel;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

/**
 * Comparable to @Configuration class in Spring.
 * All Services that are needed are provided.
 * These services are accessible with @Inject.
 */
@Module
public class ControllerModule {
    private static final String API_BASE_URL_USER = "https://poker-user-service.herokuapp.com";
    private static final String API_BASE_URL_GAME = "https://poker-game-service.herokuapp.com";
//    private static final String API_BASE_URL_USER = "http://10.0.2.2:5000";
//    private static final String API_BASE_URL_GAME = "http://10.0.2.2:5001";
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
    FragmentManager fragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    @Provides
    public SharedPrefService sharedPrefService() {
        return sharedPrefService;
    }

    @Provides
    GsonConverterFactory gsonConverter() {
        return GsonConverterFactory.create();
    }

    @Provides
    public Gson gson() {
        return new Gson();
    }

    @Provides
    EmailValidator emailValidator() { return new EmailValidator(); }

    @Provides
    public UsernameValidator usernameValidator() { return new UsernameValidator(); }

    @Provides
    OkHttpClient okHttpClient() {
        Token token = sharedPrefService().getToken(activity());
        if (token == null) return new OkHttpClient();
        return new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token.getAccessToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }

    @Provides
    UserService userService() {
        return new Retrofit
                .Builder()
                .client(okHttpClient())
                .addConverterFactory(gsonConverter())
                .baseUrl(API_BASE_URL_USER)
                .build()
                .create(UserService.class);
    }

    @Provides
    GameService gameService() {
        return new Retrofit
                .Builder()
                .client(okHttpClient())
                .addConverterFactory(gsonConverter())
                .baseUrl(API_BASE_URL_GAME)
                .build()
                .create(GameService.class);
    }

    @Provides
    @Singleton
    StompClient stompClient() {
        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://poker-game-service.herokuapp.com/connect/websocket");
        stompClient.withClientHeartbeat(10000).withServerHeartbeat(10000);
        stompClient.connect();
        return stompClient;
    }

    @Provides
    @Singleton
    WebSocketService webSocketService(StompClient stompClient) {
        return new WebSocketService(stompClient);
    }

    @Provides
    @Singleton
    ChatService chatService(WebSocketService webSocketService) {
        return new ChatService(webSocketService);
    }

    @Provides
    @Singleton
    RoomViewModel roomViewModel(WebSocketService webSocketService){
        return new RoomViewModel(webSocketService, gameService());
    }

    @Provides
    UserViewModel userViewModel(UserService userService){
        return new UserViewModel(userService);
    }

    @Provides
    @Named("UserViewModel")
    ViewModelProvider.Factory userViewModelFactory(UserViewModel viewModel){
        return new ViewModelProviderFactory<>(viewModel);
    }

    @Provides
    @Named("RoomViewModel")
    ViewModelProvider.Factory roomViewModelFactory(RoomViewModel viewModel){
        return new ViewModelProviderFactory<>(viewModel);
    }
}