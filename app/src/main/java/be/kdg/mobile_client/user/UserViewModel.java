package be.kdg.mobile_client.user;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.user.model.User;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

/**
 * ViewModel for users (directly bound to the layout)
 * This class is used so fetched data is preserved on screen rotate.
 */
public class UserViewModel extends ViewModel {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final UserService userService;
    private final Context app;
    @Getter MutableLiveData<User> user;
    private MutableLiveData<List<User>> users;
    @Getter MutableLiveData<String> message = new MutableLiveData<>();

    @Inject
    public UserViewModel(UserService userService) {
        this.userService = userService;
        this.app = App.getContext();
    }

    public LiveData<User> getUser(String id) {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser(id);
        }
        return user;
    }

    public LiveData<List<User>> getUsers(String name) {
        users = new MutableLiveData<>();
        loadUsers(name);
        return users;
    }

    public void changeUser(User user) {
        compositeDisposable.add(userService.changeUser(user).subscribe(response -> {
            message.postValue(app.getString(R.string.user_was_updated));
        }, throwable -> handleError(throwable, app.getString(R.string.change_user_tag), app.getString(R.string.error_updating_user))));
    }

    private void loadUser(String id) {
        compositeDisposable.add(userService.getUser(id).subscribe(value -> user.postValue(value),
                throwable -> handleError(throwable, app.getString(R.string.load_user_tag), app.getString(R.string.error_loading_user))));
    }

    private void loadUsers(String name) {
        compositeDisposable.add(userService.getUsersByName(name).subscribe(users::postValue,
                throwable -> handleError(throwable, app.getString(R.string.load_user_tag), app.getString(R.string.error_loading_user))));
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