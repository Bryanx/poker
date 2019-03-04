package be.kdg.userservice.user.controller.dto;

import lombok.Data;

@Data
public class AuthDTO {
    private String username;
    private String email;
    private String password;
}
