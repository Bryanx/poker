package be.kdg.userservice.user.boot;

import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import be.kdg.userservice.user.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Class that creates default users at startup.
 */
@Component
public final class CreateDefaultUserListener implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger LOGGER = LoggerFactory.getLogger(CreateDefaultUserListener.class);

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateDefaultUserListener(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Users will be made at application startup.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //Creating Users
        LOGGER.info("Creating user with role USER");
        User user = new User();
        user.setEmail("remi@test.com");
        user.setEnabled(1);
        user.setUsername("remismeets");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setChips(20000);
        userRepository.save(user);
        UserRole role = new UserRole(user.getId(), "ROLE_USER");
        userRoleRepository.save(role);

        LOGGER.info("Creating user with role USER");
        user = new User();
        user.setEmail("jarne@test.com");
        user.setEnabled(1);
        user.setUsername("jarne");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setChips(20000);
        userRepository.save(user);
        role = new UserRole(user.getId(), "ROLE_USER");
        userRoleRepository.save(role);

        //Creating Admin
        LOGGER.info("Creating user with role ADMIN");
        user = new User();
        user.setEmail("test@test.com");
        user.setEnabled(1);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("12345"));
        user.setChips(20000);
        userRepository.save(user);
        role = new UserRole(user.getId(), "ROLE_ADMIN");
        userRoleRepository.save(role);
    }
}
