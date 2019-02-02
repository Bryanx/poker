package be.kdg.userservice.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String id;
    private String userName;
    private String email;
}
