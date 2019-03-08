package be.kdg.mobile_client.user;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class UserRepository {
    private final String TAG = "UserRepository";
    private UserService userService;
    private String onErrorMsg;

    @Inject
    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Observable<User> getUser(String userId) {
        onErrorMsg = "Could not fetch user: " + userId;
        return userService.getUser(userId)
                .doOnError(this::logError)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void logError(Throwable error) {
        Log.e(TAG, onErrorMsg);
        if (error != null) {
            Log.e(TAG, error.getMessage());
            error.printStackTrace();
        }
    }
}
