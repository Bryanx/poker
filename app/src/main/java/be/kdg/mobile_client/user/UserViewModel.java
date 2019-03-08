package be.kdg.mobile_client.user;

import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.shared.CallbackWrapper;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import retrofit2.Response;

/**
 * ViewModel for users (directly bound to the layout)
 * This class is used so fetched data is preserved on screen rotate.
 */
@RequiredArgsConstructor
public class UserViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
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
        compositeDisposable.add(userService.changeUser(user).subscribe(response -> {
            if (response != null) {
                message.postValue("User was updated");
            }
        }, throwable -> handleError(throwable, "changeUser", "Error updating user")));
    }

    private void loadUser(String id) {
        compositeDisposable.add(userService.getUser(id).subscribe(response -> {
            if (response != null) {
                user.postValue(response);
            }
        }, throwable -> handleError(throwable, "loadUser", "Error loading user")));
    }

    private void loadUsers(String name) {
        compositeDisposable.add(userService.getUsersByName(name).subscribe(response -> {
            if (response != null) {
                users.postValue(response);
            }
        }, throwable -> handleError(throwable, "loadUsers", "Error loading users")));
    }

    private void handleError(Throwable throwable, String tag, String msg) {
        if (throwable != null) Log.e(tag, throwable.getMessage());
        message.postValue(msg);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}