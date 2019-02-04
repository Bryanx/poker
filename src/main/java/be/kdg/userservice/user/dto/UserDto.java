package be.kdg.userservice.user.dto;

import lombok.Data;

import java.util.List;


@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
}
