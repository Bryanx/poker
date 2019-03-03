package be.kdg.userservice.user.service;

import be.kdg.userservice.UtilTesting;
import be.kdg.userservice.user.model.User;
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

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    public void getUsersByName() {
        assertEquals(2, userService.getUsersByName(TESTABLE_USER_NAME2).size());
        assertTrue(userService.getUsersByName(TESTABLE_USER_NAME2).get(0).getUsername().contains(TESTABLE_USER_NAME2));
    }

    //TODO: fix test.
    /*
    @Test
    public void addFriends() throws Exception {
        //Prep
        User test1 = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        User test2 = userRepository.findById(testableUserId2).orElseThrow(Exception::new);

        //Add friend
        test1.setFriends(new ArrayList<>(Collections.singletonList(test2)));
        userService.changeUser(test1);

        //Test
        test1 = userRepository.findById(testableUserId1).orElseThrow(Exception::new);
        assertNotNull(test1.getFriends());
        assertEquals(1, test1.getFriends().size());
        assertEquals(testableUserId2, test1.getFriends().get(0).getId());
    }
    */
}
