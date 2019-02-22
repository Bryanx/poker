package be.kdg.userservice.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationSocketController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/user/")
    public void sendNotification() {

    }
}
