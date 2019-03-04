package be.kdg.userservice.user.controller.dto;

import lombok.Data;

@Data
public class SocialUserDTO {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String profilePictureSocial;
    private String provider;
}
