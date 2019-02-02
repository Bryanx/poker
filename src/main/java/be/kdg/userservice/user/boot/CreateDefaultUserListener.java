package be.kdg.userservice.user.boot;

import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRolesRepository;
import be.kdg.userservice.user.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CreateDefaultUserListener implements ApplicationListener<ContextRefreshedEvent> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CreateDefaultUserListener(UserRepository userRepository, UserRolesRepository userRolesRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRolesRepository = userRolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //Creating User
        logger.info("Creating user with role USER");
        User user = new User();
        user.setEmail("test@test.com");
        user.setEnabled(1);
        user.setUserName("remismeets");
        user.setPassword(passwordEncoder.encode("12345"));
        userRepository.save(user);

        UserRole role = new UserRole();
        role.setRole("ROLE_USER");
        role.setUserId(user.getId());
        userRolesRepository.save(role);

        //Creating Admin
        logger.info("Creating user with role ADMIN");
        user = new User();
        user.setEmail("test@test.com");
        user.setEnabled(1);
        user.setUserName("admin");
        user.setPassword(passwordEncoder.encode("1q2w3e"));
        userRepository.save(user);

        role = new UserRole();
        role.setRole("ROLE_ADMIN");
        role.setUserId(user.getId());
        userRolesRepository.save(role);
    }
}
