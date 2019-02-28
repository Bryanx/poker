package be.kdg.userservice.notification.controller;

import be.kdg.userservice.UtilTesting;
import be.kdg.userservice.notification.persistence.NotificationRepository;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureMockMvc
public class NotificationApiControllerTest extends UtilTesting {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Before
    public void setup() {
        provideTestingData(userRepository, userRoleRepository);
    }

    @Test
    public void acceptNotification() throws Exception {
        //TODO fix this test.
        /*
        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        testMockMvc("/user/notifications/" + testableNotificationId1 + "/accept-notification", "",
                mockMvc, RequestType.PATCH, user, authorizationServerTokenServices);
        Notification notification = notificationRepository.findById(testableNotificationId1).orElseThrow(Exception::new);
        assertTrue(notification.isRead());
        */
    }
}
