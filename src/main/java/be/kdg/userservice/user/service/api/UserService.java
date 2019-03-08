package be.kdg.userservice.user.service.api;

import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserService {
    User findUserById(String id);

    List<User> getUsers(String role);

    List<User> getUsersByName(String name);

    User addUser(User user) throws UserException;

    User changeUser(User user) throws UserException;

    User changePassword(User user) throws UserException;

    User checkSocialUser(User user) throws UserException;

    User changeUserRoleToAdmin(User user) throws UserException;

    User changeUserRoleToUser(User user) throws UserException;

    User addExperience(String id, int xp);
}
