package be.kdg.gameservice.room.controller.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private int enabled;
    private String profilePicture;
    private String profilePictureSocial;
    private String provider;
    private UserDTO[] friends;
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
}
