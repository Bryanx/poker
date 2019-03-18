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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    /**
     * Adds a notification to the system.
     *
     * @param senderId   The Id of the user that send the notification.
     * @param receiverId The Id of the user that received the notification.
     * @param message    The message itself.
     * @param type       The type of message.
     * @param ref        The reference to the senders user account. Needed for the routerlink in the front end.
     * @return A newly created notification.
     * @throws UserException Thrown if the user does not exist.
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
        LOGGER.info("Adding notification with id " + notification.getId() +  " to the system.");
        return notification;
    }

    /**
     * Toggles a notification to read.
     *
     * @param id The id of the notification that needs to be toggled.
     * @return The modified notification.
     * @throws NotificationException Thrown if the notification was not found.
     */
    @Override
    public Notification readNotification(int id) throws NotificationException {
        Notification notification = getNotification(id);
        notification.setRead(true);
        LOGGER.info("Setting notification with id " + notification.getId() +  " to read.");
        return notificationRepository.save(notification);
    }

    /**
     * Gives back all the notifications for a specific user.
     *
     * @param userId The id of the user.
     * @return All the notifications of that user.
     */
    @Override
    public List<Notification> getNotificationsForUser(String userId) {
        List<Notification> notifications = userService.findUserById(userId).getNotifications();
        LOGGER.info("Getting " + notifications.size() + " notifications from user " + userId);
        return Collections.unmodifiableList(notifications);
    }

    /**
     * Gives back all the unread notifications of the a specefic user.
     *
     * @param userId The id of that user.
     * @return All the unread notifications of that user.
     */
    @Override
    public List<Notification> getUnreadNotificationsForUser(String userId) {
        return userService.findUserById(userId).getNotifications().stream()
                .filter(not -> !not.isRead())
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Deletes all the notifications of a specific user.
     *
     * @param userId The id of that user.
     * @throws UserException Thrown if the user was not found in the database.
     */
    @Override
    public void deleteAllNotifications(String userId) throws UserException {
        //Get data
        User user = userService.findUserById(userId);

        //Update data
        user.deleteAllNotifications();
        userService.changeUser(user);
        LOGGER.info("Deleting all notification for user " + userId);
    }

    /**
     * Deletes a specific notification from the account of a specific user.
     *
     * @param userId         The id of the user.
     * @param notificationId The id of the notification.
     * @throws NotificationException Thrown if the notification was not found in the database.
     * @throws UserException         Thrown if the user was not found in the database.
     */
    @Override
    public void deleteNotification(String userId, int notificationId) throws NotificationException, UserException {
        //Get data
        User user = userService.findUserById(userId);
        Notification notification = getNotification(notificationId);

        //Update data
        user.deleteNotification(notification);
        userService.changeUser(user);
        LOGGER.info("Deleting notification with id " + notification.getId() + " from user " + userId);
    }

    /**
     * Returns a specific notification.
     *
     * @param id The id of that notification.
     * @return The requested notification.
     * @throws NotificationException Thrown if the notification was not found in the database.
     */
    private Notification getNotification(int id) throws NotificationException {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(NotificationServiceImpl.class, "Notification was not found in the database."));
    }
}
