package be.kdg.userservice.notification.model;

import be.kdg.userservice.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    @Setter
    private boolean approved;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User sender;

    public Notification(String message, NotificationType type, User sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
        this.approved = false;
        this.timestamp = LocalDateTime.now();
    }
}
