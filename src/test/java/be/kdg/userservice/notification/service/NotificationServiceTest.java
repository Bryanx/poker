package be.kdg.userservice.notification.service;

import be.kdg.userservice.UtilTesting;
import be.kdg.userservice.notification.exception.NotificationException;
import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.persistence.NotificationRepository;
import be.kdg.userservice.notification.service.api.NotificationService;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class NotificationServiceTest extends UtilTesting {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Before
    public void setup() {
        provideTestingData(userRepository, userRoleRepository);
    }

    @Test
    public void getNotificationsForUser() {
        assertEquals(2, notificationService.getNotificationsForUser(testableUserId1).size());
    }

    @Test
    public void acceptNotification() throws Exception {
        notificationService.acceptNotification(testableNotificationId1);
        Notification notification = notificationRepository.findById(testableNotificationId1).orElseThrow(Exception::new);
        assertTrue(notification.isApproved());
    }

    @Test
    public void deleteNotification() throws Exception {
        notificationService.deleteNotification(testableUserId1, testableNotificationId1);
        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        assertEquals(1, user.getNotifications().size());
    }

    @Test
    public void deleteNotifications() throws Exception {
        notificationService.deleteAllNotifications(testableUserId1);
        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        assertEquals(0, user.getNotifications().size());
    }
}
