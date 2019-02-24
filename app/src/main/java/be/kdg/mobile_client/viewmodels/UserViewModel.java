package be.kdg.mobile_client.viewmodels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserViewModel extends ViewModel {
    private final UserService userService;
    @Getter MutableLiveData<User> user;
    MutableLiveData<List<User>> users;

    public LiveData<User> getUser(String id) {
        if (user == null) {
            user = new MutableLiveData<>();
            loadUser(id);
        }
        return user;
    }

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
            loadUsers();
        }
        return users;
    }

    private void loadUser(String id) {
        userService.getUser(id).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                user.setValue(response.body());
            }
        }));
    }

    private void loadUsers() {
        userService.getUsers().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                users.setValue(response.body());
            }
        }));
    }
}