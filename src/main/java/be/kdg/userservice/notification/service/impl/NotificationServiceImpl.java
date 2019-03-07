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
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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

    /**
     * Adds a notification to the system.
     *
     * @param senderId The Id of the person that send the notification.
     * @param receiverId
     * @param message
     * @param type
     * @param ref
     * @return
     * @throws UserException
     */
    @Override
    public Notification addNotification(String senderId, String receiverId, String message, NotificationType type, String ref) throws UserException {
        //Get data
        User receiver = userService.findUserById(receiverId);

        //Construct message
        Notification notification = new Notification(message, type, ref);
        receiver.addNotification(notification);

        //Save data
        userService.changeUser(receiver);
        notification = notificationRepository.save(notification);
        return notification;
    }

    @Override
    public Notification readNotification(int id) throws NotificationException {
        Notification notification = getNotification(id);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsForUser(String userId) {
        List<Notification> notifications = userService.findUserById(userId).getNotifications();
        return Collections.unmodifiableList(notifications);
    }

    @Override
    public List<Notification> getNotificationsForType(NotificationType type) {
        return Collections.unmodifiableList(notificationRepository.findAllByType(type));
    }

    @Override
    public List<Notification> getUnreadNotificationsForUser(String userId) {
        return userService.findUserById(userId).getNotifications().stream()
                .filter(not -> !not.isRead())
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    public void deleteAllNotifications(String userId) throws UserException {
        //Get data
        User user = userService.findUserById(userId);

        //Update data
        user.deleteAllNotifications();
        userService.changeUser(user);
    }

    @Override
    public void deleteNotification(String userId, int notificationId) throws NotificationException, UserException {
        //Get data
        User user = userService.findUserById(userId);
        Notification notification = getNotification(notificationId);

        //Update data
        user.deleteNotification(notification);
        userService.changeUser(user);
    }

    private Notification getNotification(int id) throws NotificationException {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(NotificationServiceImpl.class, "Notification was not found in the database."));
    }
}
