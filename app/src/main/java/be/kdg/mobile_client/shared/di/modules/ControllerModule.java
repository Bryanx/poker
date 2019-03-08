package be.kdg.mobile_client.shared.di.modules;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import be.kdg.mobile_client.chat.ChatService;
import be.kdg.mobile_client.room.RoomRepository;
import be.kdg.mobile_client.room.RoomService;
import be.kdg.mobile_client.room.RoomViewModel;
import be.kdg.mobile_client.round.RoundRepository;
import be.kdg.mobile_client.round.RoundService;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.shared.ViewModelProviderFactory;
import be.kdg.mobile_client.shared.WebSocketService;
import be.kdg.mobile_client.shared.validators.EmailValidator;
import be.kdg.mobile_client.shared.validators.UsernameValidator;
import be.kdg.mobile_client.user.UserRepository;
import be.kdg.mobile_client.user.UserService;
import be.kdg.mobile_client.user.UserViewModel;
import be.kdg.mobile_client.user.authorization.AuthorizationService;
import be.kdg.mobile_client.user.authorization.Token;
import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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
    private static final String WEBSOCKET_URL = "wss://poker-game-service.herokuapp.com/connect/websocket";
    private static final int WEBSOCKET_HEARTBEAT_MS = 10000;
//    private static final String API_BASE_URL_USER = "http://10.0.2.2:5000";
//    private static final String API_BASE_URL_GAME = "http://10.0.2.2:5001";
//    private static final String WEBSOCKET_URL = "ws://10.0.2.2:5001/connect/websocket";
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
    Gson gson() {
        return new Gson();
    }

    @Provides
    EmailValidator emailValidator() { return new EmailValidator(); }

    @Provides
    UsernameValidator usernameValidator() { return new UsernameValidator(); }

    @Provides
    OkHttpClient okHttpClient() {
        Token token = sharedPrefService().getToken(activity());
        if (token == null) return new OkHttpClient();
        return new OkHttpClient().newBuilder().addInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token.getAccessToken())
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("Accept", "application/json;charset=utf-8")
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(API_BASE_URL_USER)
                .build()
                .create(UserService.class);
    }

    @Provides
    RoomService roomService() {
        return new Retrofit
                .Builder()
                .client(okHttpClient())
                .addConverterFactory(gsonConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(API_BASE_URL_GAME)
                .build()
                .create(RoomService.class);
    }

    @Provides
    RoundService roundService() {
        return new Retrofit
                .Builder()
                .client(okHttpClient())
                .addConverterFactory(gsonConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(API_BASE_URL_GAME)
                .build()
                .create(RoundService.class);
    }

    @Provides
    AuthorizationService authorizationService() {
        return new Retrofit
                .Builder()
                .client(okHttpClient())
                .addConverterFactory(gsonConverter())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(API_BASE_URL_USER)
                .build()
                .create(AuthorizationService.class);
    }

    @Provides
    @Singleton
    StompClient stompClient() {
        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_URL);
        stompClient.withClientHeartbeat(WEBSOCKET_HEARTBEAT_MS).withServerHeartbeat(WEBSOCKET_HEARTBEAT_MS);
        stompClient.connect();
        return stompClient;
    }

    @Provides
    @Singleton
    WebSocketService webSocketService() {
        return new WebSocketService();
    }

    @Provides
    @Singleton
    ChatService chatService(WebSocketService webSocketService) {
        return new ChatService(webSocketService);
    }

    @Provides
    UserRepository userRepository(UserService userService) {
        return new UserRepository(userService);
    }
    @Provides
    RoomRepository roomRepository(WebSocketService webSocketService) {
        return new RoomRepository(roomService(), webSocketService);
    }

    @Provides
    RoundRepository roundRepository(WebSocketService webSocketService) {
        return new RoundRepository(webSocketService, roundService());
    }

    @Provides
    @Singleton
    RoomViewModel roomViewModel(RoomRepository roomRepo, RoundRepository roundRepo){
        return new RoomViewModel(roomRepo, roundRepo);
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