package be.kdg.userservice.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple class to store the token of a user.
 */
@Data
@AllArgsConstructor
public class TokenDto {
    private String access_token;
    private int expires_in;
}
