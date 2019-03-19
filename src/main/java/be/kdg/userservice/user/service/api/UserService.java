package be.kdg.userservice.user.service.api;

import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.Friend;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;


import java.util.List;


public interface UserService {
    User findUserById(String id);

    List<User> getUsers(String role);

    List<User> getUsersByName(String name);

    User addUser(User user) throws UserException;

    User changeUser(User user) throws UserException;

    User addFriend(String userId, Friend friend) throws UserException;

    User deleteFriend(String userId, String userIdOfFriend) throws UserException;

    User changePassword(User user) throws UserException;

    User checkSocialUser(User user) throws UserException;

    User changeRole(User user, String role);

    User addExperience(String id, int xp);

    User addWin(String id);

    void addGamesPlayed(List<String> ids);
}
