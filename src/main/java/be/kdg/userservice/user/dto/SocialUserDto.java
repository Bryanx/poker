package be.kdg.userservice.user.dto;

import lombok.Data;

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
