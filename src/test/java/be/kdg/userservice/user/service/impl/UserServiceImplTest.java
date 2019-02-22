package be.kdg.userservice.user.service.impl;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        userRepository.deleteAll();
        User user1 = new User();
        user1.setEmail("test@testtest.com");
        user1.setEnabled(1);
        user1.setUsername("test1");
        user1.setPassword(passwordEncoder.encode("12345"));

        User user2 = new User();
        user2.setEmail("test@testtesttest.com");
        user2.setEnabled(1);
        user2.setUsername("test2");
        user2.setPassword(passwordEncoder.encode("1q2w3e"));

        userRepository.save(user1);
        userRoleRepository.save(new UserRole(user1.getId(), "ROLE_USER"));
        userRepository.save(user2);
        userRoleRepository.save(new UserRole(user2.getId(), "ROLE_USER"));
    }

    @Test
    public void addFriends() throws Exception {
        //Prep
        User test1 = userRepository.findByUsername("test1").orElseThrow(Exception::new);
        User test2 = userRepository.findByUsername("test2").orElseThrow(Exception::new);

        //Add friend
        test1.setFriends(new ArrayList<>(Collections.singletonList(test2)));
        userService.changeUser(test1);

        //Test
        test1 = userRepository.findByUsername("test1").orElseThrow(Exception::new);
        assertNotNull(test1.getFriends());
        assertTrue(test1.getFriends().iterator().next().getUsername().equalsIgnoreCase("test2"));
        assertTrue(test1.getFriends().iterator().next().getEmail().equalsIgnoreCase("test@testtesttest.com"));
    }
}