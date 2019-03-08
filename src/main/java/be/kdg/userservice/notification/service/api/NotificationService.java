package be.kdg.userservice.notification.service.api;

import be.kdg.userservice.notification.exception.NotificationException;
import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.model.NotificationType;
import be.kdg.userservice.user.exception.UserException;

import java.util.List;

public interface NotificationService {
    Notification addNotification(String senderId, String receiverId, String message, NotificationType type, String ref) throws UserException;

    Notification readNotification(int id) throws NotificationException;

    List<Notification> getNotificationsForUser(String userId);

    List<Notification> getUnreadNotificationsForUser(String userId);

    void deleteAllNotifications(String userId) throws UserException;

    void deleteNotification(String userId, int notificationId) throws NotificationException, UserException;
}
