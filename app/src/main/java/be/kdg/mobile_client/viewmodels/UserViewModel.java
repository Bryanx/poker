package be.kdg.mobile_client.viewmodels;

import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import retrofit2.Response;

/**
 * ViewModel for users (directly bound to the layout)
 * This class is used so fetched data is preserved on screen rotate.
 */
@RequiredArgsConstructor
public class UserViewModel extends ViewModel {
    private final UserService userService;
    @Getter MutableLiveData<User> user;
    private MutableLiveData<List<User>> users;
    @Getter MutableLiveData<String> message = new MutableLiveData<>();

    public LiveData<User> getUser(String id) {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser(id);
        }
        return user;
    }

    public LiveData<List<User>> getUsers(String name) {
        if (users == null) {
            users = new MutableLiveData<>();
            loadUsers(name);
        }
        return users;
    }

    public void changeUser(User user) {
        userService.changeUser(user).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                message.setValue("User was updated");
            } else {
                handleError(throwable, "changeUser", "Error updating user");
            }
        }));
    }

    private void loadUser(String id) {
        userService.getUser(id).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                user.setValue(response.body());
            } else {
                handleError(throwable, "loadUser", "Error loading user");
            }
        }));
    }

    private void loadUsers(String name) {
        userService.getUsersByName(name).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                users.setValue(response.body());
            } else {
                handleError(throwable, "loadUsers", "Error loading users");
            }
        }));
    }

    private boolean responseSuccess(Response response) {
        return response != null && response.body() != null && response.isSuccessful();
    }

    private void handleError(Throwable throwable, String tag, String msg) {
        if (throwable != null) Log.e(tag, throwable.getMessage());
        message.setValue(msg);
    }
}