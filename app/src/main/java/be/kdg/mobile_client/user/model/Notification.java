package be.kdg.mobile_client.user.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Setter;

@Data
public class Notification {
    private int id;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    @Setter
    private boolean read;
    private String ref;
}
