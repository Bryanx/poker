package be.kdg.userservice.user.controller.dto;

import lombok.Data;

/**
 * DTO that us user internally for authentication-.
 */
@Data
public class AuthDto {
    private String username;
    private String email;
    private String password;
}
