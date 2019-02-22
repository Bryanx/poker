package be.kdg.userservice.notification.service.impl;

import be.kdg.userservice.notification.exception.NotificationException;
import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.model.NotificationType;
import be.kdg.userservice.notification.persistence.NotificationRepository;
import be.kdg.userservice.notification.service.api.NotificationService;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * This class is used to manage everything that has something to do with notifications.
 * Some of this methods will be called from a web-socket, others form a normal api.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @Override
    public Notification addNotification(String senderId, String receiverId, String message, NotificationType type) throws UserException {
        //Get data
        User sender = userService.findUserById(senderId);
        User receiver = userService.findUserById(receiverId);

        //Construct message
        Notification notification = new Notification(message, type, sender);
        receiver.addNotification(notification);

        //Save data
        userService.changeUser(receiver);
        return notification;
    }

    @Override
    public Notification acceptNotification(int id) throws NotificationException {
        Notification notification = getNotification(id);
        notification.setApproved(true);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(String userId) {
        List<Notification> notifications = userService.findUserById(userId).getNotifications();
        return Collections.unmodifiableList(notifications);
    }

    @Override
    public void deleteAllNotifications(String userId) {
        userService.findUserById(userId).deleteAllNotifications();
    }

    @Override
    public void deleteAllNotification(int id) throws NotificationException {
        Notification notification = getNotification(id);
        notificationRepository.delete(notification);
    }

    private Notification getNotification(int id) throws NotificationException {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(NotificationServiceImpl.class, "Notification was not found in the database."));
    }
}
