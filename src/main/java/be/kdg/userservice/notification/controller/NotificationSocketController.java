package be.kdg.userservice.notification.controller;

import be.kdg.userservice.notification.controller.dto.NotificationDTO;
import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.service.api.NotificationService;
import be.kdg.userservice.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class NotificationSocketController {
    private final SimpMessagingTemplate template;
    private final ModelMapper modelMapper;
    private final NotificationService notificationService;

    @MessageMapping("/user/{receiverId}/send-friend-request")
    public void sendFriendRequest(@DestinationVariable("receiverId") String receiverId, @Valid NotificationDTO notificationDTO) throws UserException {
        Notification notificationIn = notificationService.addNotification(receiverId, notificationDTO.getSender().getId(), notificationDTO.getMessage(), notificationDTO.getType());
        NotificationDTO notificationOut = modelMapper.map(notificationIn, NotificationDTO.class);
        this.template.convertAndSend("/user/receive-notification/" + receiverId, notificationOut);
    }
}
