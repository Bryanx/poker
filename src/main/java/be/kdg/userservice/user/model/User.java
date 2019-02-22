package be.kdg.userservice.user.model;

import be.kdg.userservice.notification.model.Notification;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @Getter
    private String id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String firstname;

    @Getter
    @Setter
    private String lastname;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private int enabled;

    @Getter
    @Setter
    private byte[] profilePictureBinary;

    @Getter
    private String socialId;

    @Getter
    private String profilePictureSocial;

    @Getter
    private String provider;

    @Getter
    @Setter
    private int chips;

    @Getter
    private int wins;

    @Getter
    private int gamesPlayed;

    @Getter
    private String bestHand;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Notification> notifications;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<User> friends;

    public User() {
        this.notifications = new ArrayList<>();
    }

    public List<User> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    public void replaceFriends(List<User> friends) {
        this.friends = new ArrayList<>(friends);
    }

    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public void deleteAllNotifications() {
        notifications.clear();
    }
}
