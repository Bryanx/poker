package be.kdg.userservice.user.controller.dto;

import be.kdg.userservice.user.model.Friend;
import lombok.Data;

/**
 * Standard user DTO user for sending user information to the front end.
 */
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
    private Friend[] friends;
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
    private int enabled;
    private int level;
    private int thresholdTillNextLevel;
    private int xpTillNext;
}
