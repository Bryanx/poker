package be.kdg.mobile_client.user;

import javax.inject.Inject;
import javax.inject.Singleton;

import be.kdg.mobile_client.user.User;
import be.kdg.mobile_client.user.UserService;
import retrofit2.Call;

@Singleton
public class UserRepository {
    private UserService userService;

    @Inject
    public UserRepository(UserService userService) {
        this.userService = userService;
    }

    public Call<User> getUser(String userId) {
        return userService.getUser(userId);
    }
}
