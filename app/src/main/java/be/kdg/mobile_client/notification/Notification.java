package be.kdg.mobile_client.notification;

import lombok.Data;

@Data
class Notification {
    private int id;
    private String message;
    private NotificationType type;
    private String timestamp;

    private boolean read;
    private String ref;
}
