package be.kdg.userservice.user.service.impl;

import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import be.kdg.userservice.user.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Before
    public void setUp() throws Exception {
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
    public void addFriends() throws UserException {
        Optional<User> userTest1 = userRepository.findByUsername("test1");
        Optional<User> userTest2 = userRepository.findByUsername("test2");
        if (!userTest1.isPresent() || !userTest2.isPresent()) throw new UserException("oops");
        User test1 = userTest1.get();
        User test2 = userTest2.get();
        test1.setFriends(Arrays.asList(test2));
        userRepository.saveAll(Arrays.asList(test1,test2));
        test1 = userService.findUserById(test1.getId());
        assertNotNull(test1.getFriends());
        assertTrue(test1.getFriends().get(0).getUsername().equalsIgnoreCase("test2"));
        assertTrue(test1.getFriends().get(0).getEmail().equalsIgnoreCase("test@testtesttest.com"));
    }
}