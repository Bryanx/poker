package be.kdg.mobile_client.user.model;

import java.util.List;

import be.kdg.mobile_client.friends.Friend;
import lombok.Data;

@Data
public class User implements Comparable<User> {
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private int enabled;
    private String profilePicture;
    private String socialId;
    private String profilePictureSocial;
    private String provider;
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
    private List<Friend> friends;
    private int level;
    private int thresholdTillNextLevel;
    private int xpTillNext;

    @Override
    public int compareTo(User o) {
        return o.chips - this.chips;
    }

    public void addFriend(Friend friend) {
        friends.add(friend);
    }
}
