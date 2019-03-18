package be.kdg.userservice.user.model;

import be.kdg.userservice.notification.model.Notification;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the micro service.
 * This class holds information about things like its credentials and many other things like:
 * - Notifications
 * - Friends
 * - Social provider
 *
 * @see Friend
 * @see Notification
 */
@Data
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    private String id;
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private int enabled;
    private byte[] profilePictureBinary;
    private String socialId;
    private String profilePictureSocial;
    private String provider;
    private int chips;
    private int wins;
    private int gamesPlayed;
    private String bestHand;
    private int level;
    private int thresholdTillNextLevel;
    private int xpTillNext;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Notification> notifications;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Friend> friends;

    public User() {
        this.level = 1;
        this.thresholdTillNextLevel = 100;
        this.friends = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void deleteNotification(Notification notification) {
        notifications.remove(notification);
    }

    public void deleteAllNotifications() {
        notifications.clear();
    }
}
