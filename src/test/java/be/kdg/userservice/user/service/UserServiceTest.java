package be.kdg.userservice.user.service;

import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import be.kdg.userservice.user.service.api.UserService;
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
public class UserServiceTest {
    private static final String TEST_NAME1 = "josef";
    private static final String TEST_NAME2 = "anne";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        User testUser1 = new User();
        testUser1.setUsername(TEST_NAME1);
        User testUser2 = new User();
        testUser2.setUsername(TEST_NAME1);
        User testUser3 = new User();
        testUser3.setUsername(TEST_NAME2);
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        UserRole ur1 = new UserRole(testUser1.getId(), "ROLE_USER");
        UserRole ur2 = new UserRole(testUser2.getId(), "ROLE_USER");
        UserRole ur3 = new UserRole(testUser3.getId(), "ROLE_USER");
        userRoleRepository.save(ur1);
        userRoleRepository.save(ur2);
        userRoleRepository.save(ur3);
    }

    @Test
    public void getUsersByName() {
        assertEquals(2, userService.getUsersByName(TEST_NAME1).size());
        assertEquals(TEST_NAME1, userService.getUsersByName(TEST_NAME1).get(0).getUsername());
    }
}
