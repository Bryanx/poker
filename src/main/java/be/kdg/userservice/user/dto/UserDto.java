package be.kdg.userservice.user.dto;

import lombok.Data;



@Data
public class UserDto {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String profilePicture;
    private String profilePictureSocial;
    private String provider;
    private UserDto[] friends;
}
