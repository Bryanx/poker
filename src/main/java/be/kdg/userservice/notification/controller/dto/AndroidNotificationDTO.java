package be.kdg.userservice.notification.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AndroidNotificationDTO {
    private String to;
    private NotificationAndroidDTO notification;

    public AndroidNotificationDTO(String to, String title, String text) {
        this.to = to;
        this.notification = new NotificationAndroidDTO(title, text);
    }

    @Data
    @AllArgsConstructor
    private class NotificationAndroidDTO {
        private String title;
        private String text;
    }
}
