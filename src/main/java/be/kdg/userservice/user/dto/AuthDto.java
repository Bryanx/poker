package be.kdg.userservice.user.dto;

import lombok.Data;

@Data
public class AuthDto {
    private String username;
    private String password;
}
