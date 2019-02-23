package be.kdg.userservice.notification.controller.dto;

import be.kdg.userservice.notification.model.NotificationType;
import be.kdg.userservice.user.dto.UserDto;
import be.kdg.userservice.user.model.User;
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
    private UserDto sender;
    private String message;
    private NotificationType type;
}
