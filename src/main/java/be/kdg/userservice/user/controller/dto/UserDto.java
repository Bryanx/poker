package be.kdg.userservice.user.controller.dto;

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
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
    private int enabled;
}
