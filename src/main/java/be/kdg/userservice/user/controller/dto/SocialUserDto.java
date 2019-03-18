package be.kdg.userservice.user.controller.dto;

import lombok.Data;

/**
 * DTO that contains the information of a user that was logged in using a social provider like
 * facebook or google.
 */
@Data
public class SocialUserDto {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String profilePictureSocial;
    private String provider;
}
