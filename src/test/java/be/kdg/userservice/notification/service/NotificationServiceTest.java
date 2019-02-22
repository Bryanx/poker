package be.kdg.userservice.notification.service;

import be.kdg.userservice.UtilTesting;
import be.kdg.userservice.notification.service.api.NotificationService;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class NotificationServiceTest extends UtilTesting {
    @Autowired
    private NotificationService notificationService;
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
        assertEquals(1, notificationService.getNotificationsForUser(testableUserId1).size());
    }
}
