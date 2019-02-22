package be.kdg.mobile_client.model;

import java.util.List;

import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private List<User> friends;
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
    private String profilePicture;
    private String profilePictureSocial;
}
