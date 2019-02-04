package be.kdg.userservice.user.service.api;

import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;


public interface UserService {
    User findUserById(String id);
    User addUser(User user) throws UserException;
}
