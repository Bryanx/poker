package be.kdg.userservice;

import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.model.NotificationType;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
@Transactional
public abstract class UtilTesting {
    @Value("${token.url}")
    private String TOKEN_URL;

    private static final String TESTABLE_USER_NAME1 = "josef";
    private static final String TESTABLE_USER_NAME3 = "anne";
    protected static final String TESTABLE_USER_NAME2 = "jos";
    protected static final String USER_ROLE = "ROLE_USER";
    protected static final String ADMIN_ROLE = "ROLE_ADMIN";

    protected String testableUserId1;
    protected String testableUserId2;
    protected int testableNotificationId1;

    /**
     * This method will provide some test-data that will be used by the test-classes that extend from it.
     *
     * @param userRepository     The repository for users.
     * @param userRoleRepository The repository for user roles.
     */
    protected void provideTestingData(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        User testUser1 = new User();
        testUser1.setUsername("test");
        testUser1.setUsername(TESTABLE_USER_NAME1);
        User testUser2 = new User();
        testUser2.setUsername(TESTABLE_USER_NAME2);
        User testUser3 = new User();
        testUser3.setUsername(TESTABLE_USER_NAME3);
        User admin = new User();
        testUser3.setUsername("ADMIN");
        testUser1.addNotification(new Notification("Test message", NotificationType.FRIEND_REQUEST, ""));
        testUser1.addNotification(new Notification("Test message", NotificationType.GAME_REQUEST, ""));
        testUser1.addNotification(new Notification("test admin message", NotificationType.GLOBAL_MESSAGE, ""));
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        userRepository.save(admin);

        UserRole ur1 = new UserRole(testUser1.getId(), USER_ROLE);
        UserRole ur2 = new UserRole(testUser2.getId(), USER_ROLE);
        UserRole ur3 = new UserRole(testUser3.getId(), USER_ROLE);
        UserRole adminRole = new UserRole(admin.getId(), ADMIN_ROLE);
        userRoleRepository.save(ur1);
        userRoleRepository.save(ur2);
        userRoleRepository.save(ur3);
        userRoleRepository.save(ur3);
        userRoleRepository.save(adminRole);

        testableUserId1 = testUser1.getId();
        testableUserId2 = testUser3.getId();
        testableNotificationId1 = testUser1.getNotifications().get(0).getId();
    }
}
