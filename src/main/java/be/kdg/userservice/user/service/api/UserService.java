package be.kdg.userservice.user.service.api;

import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User findUserById(String id);
}
