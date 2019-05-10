package be.kdg.userservice.notification.controller.dto;

import be.kdg.userservice.notification.model.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private int id;
    @Valid
    private String message;
    @Valid
    private NotificationType type;
    private boolean read;
    private String timestamp;
    private String ref;
}
