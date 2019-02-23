package be.kdg.userservice.user.dto;

import be.kdg.userservice.notification.controller.dto.NotificationDTO;
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
    private NotificationDTO[] notifications;
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
}
