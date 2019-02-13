package be.kdg.userservice.user.service.impl;

import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.shared.security.model.CustomUserDetails;
import be.kdg.userservice.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class that handles all user related tasks.
 */
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isPresent()) {
            List<String> userRoles = userRoleRepository.findRoleByUserName(username);
            return new CustomUserDetails(optionalUser.get(), userRoles);
        }

        throw new UsernameNotFoundException("Bad credentials");
    }

    @Override
    public User findUserById(String id) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Bad credentials");
        } else {
            return user.get();
        }
    }

    @Override
    public List<User> getUsers() {
        return Collections.unmodifiableList(userRepository.findAll());
    }

    @Override
    public List<User> getUsersByName(String name) {
       return getUsers().stream()
               .filter(u -> u.getUsername().contains(name))
               .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public User addUser(User user) throws UserException{
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            throw new UserException("User already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(1);
        user = userRepository.save(user);

        UserRole role = new UserRole(user.getId(), "ROLE_USER");
        userRoleRepository.save(role);

        return user;
    }

    @Override
    public User changeUser(User user) throws UserException {
        User dbUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException("User not found"));

        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isPresent()) {
            if (!optionalUser.get().getUsername().equals(user.getUsername()))
            {
                throw new UserException("Username already taken");
            }
        }

        dbUser.setUsername(user.getUsername());
        dbUser.setFirstname(user.getFirstname());
        dbUser.setLastname(user.getLastname());
        dbUser.setEmail(user.getEmail());
        dbUser.setProfilePictureBinary(user.getProfilePictureBinary());

        return userRepository.save(dbUser);
    }

    @Override
    public User changePassword(User user) throws UserException {
        User dbUser = userRepository.findByUsername(user.getUsername()).orElseThrow(() -> new UserException("User not found"));
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(dbUser);
    }

    @Override
    public User checkSocialUser(User user) throws UserException {
        Optional<User> dbUser = userRepository.findBySocialId(user.getSocialId());

        if (!dbUser.isPresent()) {
            user.setEnabled(1);
            user = userRepository.save(user);
            UserRole role = new UserRole(user.getId(), "ROLE_USER");
            userRoleRepository.save(role);
            return user;
        }

        return dbUser.get();
    }
}
