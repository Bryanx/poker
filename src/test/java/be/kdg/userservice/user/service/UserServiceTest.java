package be.kdg.userservice.user.service;

import be.kdg.userservice.UtilTesting;
import be.kdg.userservice.user.model.Friend;
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

import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceTest extends UtilTesting {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        provideTestingData(userRepository, userRoleRepository);
    }

    @Test
    public void getUsers() {
        List<User> users = userService.getUsers(USER_ROLE);
        assertEquals(3, users.size());
        users = userService.getUsers(ADMIN_ROLE);
        assertEquals(1, users.size());
    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setUsername("Jarne");
        user.setPassword("12345");
        user = userService.addUser(user);

        assertNotEquals(0, user.getId());
        assertEquals(20000, user.getChips());
        assertEquals(1, user.getEnabled());
        assertEquals(1, user.getLevel());
        assertEquals(100, user.getThresholdTillNextLevel());

        UserRole userRole = userRoleRepository.findByUserId(user.getId()).orElseThrow(Exception::new);
        assertEquals(USER_ROLE, userRole.getRole());
    }

    @Test
    public void changeUser() throws Exception {
        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        user.setUsername("joske");
        user.setFirstname("joske");
        user.setLastname("vermeiren");
        user.setEmail("joske@test.com");

        user = userService.changeUser(user);
        assertEquals("joske", user.getUsername());
        assertEquals("joske", user.getFirstname());
        assertEquals("vermeiren", user.getLastname());
        assertEquals("joske@test.com", user.getEmail());
    }

    @Test
    public void changeRole() throws Exception {
        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);

        userService.changeRole(user, ADMIN_ROLE);
        UserRole role = userRoleRepository.findByUserId(testableUserId1).orElseThrow(Exception::new);
        assertEquals(ADMIN_ROLE, role.getRole());

        userService.changeRole(user, USER_ROLE);
        role = userRoleRepository.findByUserId(testableUserId1).orElseThrow(Exception::new);
        assertEquals(USER_ROLE, role.getRole());
    }

    @Test
    public void getUsersByName() {
        assertEquals(2, userService.getUsersByName(TESTABLE_USER_NAME2).size());
        assertTrue(userService.getUsersByName(TESTABLE_USER_NAME2).get(0).getUsername().contains(TESTABLE_USER_NAME2));
    }

    /*
    @Test
    public void addAndDeleteFriends() throws Exception {
        //Prep
        User test1 = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        User test2 = userRepository.findById(testableUserId2).orElseThrow(Exception::new);

        //Add friend
        test1.setFriends(new ArrayList<>(Collections.singletonList(new Friend(testableUserId2))));
        test2.setFriends(new ArrayList<>(Collections.singletonList(new Friend(testableUserId1))));
        userService.changeFriends(test1);
        userService.changeFriends(test2);

        //Test
        test1 = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        test2 = userRepository.findById(testableUserId2).orElseThrow(Exception::new);

        assertNotNull(test1.getFriends());
        assertNotNull(test2.getFriends());
        assertEquals(1, test1.getFriends().size());
        assertEquals(1, test2.getFriends().size());
        assertEquals(testableUserId2, test1.getFriends().get(0).getUserId());
        assertEquals(testableUserId1, test2.getFriends().get(0).getUserId());

        //Delete friend
        List<Friend> dummyList = new ArrayList<>(); //empty list (no friends)
        test1.setFriends(dummyList);
        userService.changeFriends(test1);

        //Test
        test1 = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        assertNotNull(test1.getFriends());
        assertEquals(0, test1.getFriends().size());
    }
    */

    @Test
    public void addExperience() {
        User user1 = userService.addExperience(testableUserId1, 100);
        assertEquals(2, user1.getLevel());
        User user2 = userService.addExperience(testableUserId2, 90);
        assertEquals(1, user2.getLevel());

        //Test leveling multiple levels at once
        int chipsThen = user1.getChips();
        user1 = userService.addExperience(testableUserId1, 10000);
        assertTrue(user1.getLevel() > 6);
        assertTrue(user1.getChips() > chipsThen);
    }

    @Test
    public void addWin() throws Exception {
        userService.addWin(testableUserId1);
        userService.addWin(testableUserId1);
        userService.addWin(testableUserId1);
        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        assertEquals(3, user.getWins());
    }

    @Test
    public void addGamesPlayed() throws Exception {
        userService.addGamesPlayed(Arrays.asList(testableUserId1, testableUserId2));
        userService.addGamesPlayed(Arrays.asList(testableUserId1, testableUserId2));

        User user = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        assertEquals(2, user.getGamesPlayed());
        user = userRepository.findById(testableUserId2).orElseThrow(Exception::new);
        assertEquals(2, user.getGamesPlayed());
    }
}
