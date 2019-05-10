package be.kdg.userservice.notification.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Simple pojo class that hold the a notification
 */
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
    private boolean read;
    private String ref;

    public Notification(String message, NotificationType type, String ref) {
        this.message = message;
        this.type = type;
        this.read = false;
        this.timestamp = LocalDateTime.now();
        this.ref = ref;
    }
}
