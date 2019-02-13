package be.kdg.userservice.user.service.api;

import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;

import java.util.List;


public interface UserService {
    User findUserById(String id);

    List<User> getUsers();

    List<User> getUsersForName(String name);

    User addUser(User user) throws UserException;

    User changeUser(User user) throws UserException;

    User changePassword(User user) throws UserException;

    User checkSocialUser(User user) throws UserException;
}
