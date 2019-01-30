package be.kdg.userservice.service.api;

import be.kdg.userservice.model.User;
import be.kdg.userservice.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto user);
    List<UserDto> findAll();
    User findOne(long id);
    void delete(long id);
}
