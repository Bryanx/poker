package be.kdg.mobile_client.user;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.user.model.User;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class UserRepository {
    private static final String COULD_NOT_FETCH_USER = "Could not fetch user: ";
    private final String TAG = "UserRepository";
    private final UserService userService;
    private String onErrorMsg;

    @Inject
    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Observable<User> getUser(String userId) {
        onErrorMsg = COULD_NOT_FETCH_USER + userId;
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
