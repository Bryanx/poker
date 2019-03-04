package be.kdg.userservice.user.service.impl;

import be.kdg.userservice.shared.security.model.CustomUserDetails;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
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

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found in database"));

        List<String> userRoles = userRoleRepository.findRoleByUserName(username);
        return new CustomUserDetails(user, userRoles);
    }

    @Override
    public User findUserById(String id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found in the database"));
    }

    @Override
    public List<User> getUsers(String role) throws UserException {
        return userRepository.findAll().stream()
                .filter(user -> userRoleRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new UserException("user was not found in role repo."))
                        .getRole().equals(role))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    public List<User> getUsersByName(String name) throws UserException {
        return getUsers("ROLE_USER").stream()
                .filter(u -> u.getUsername().contains(name))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @Override
    public User addUser(User user) throws UserException {
        //Check if already exists
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) throw new UserException("User already exists");

        //Add user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setChips(20000);
        user.setEnabled(1);
        user.setLevel(1);
        user.setThresholdTillNextLevel(100);
        user = saveUser(user);
        UserRole role = new UserRole(user.getId(), "ROLE_USER");
        userRoleRepository.save(role);

        return user;
    }

    @Override
    public User changeUser(User user) throws UserException {
        User userToUpdate = userRepository.findById(user.getId()).orElseThrow(() -> new UserException("User not found"));
        Optional<User> optionalUserCheck = userRepository.findByUsername(user.getUsername());

        if (optionalUserCheck.isPresent() && !optionalUserCheck.get().getUsername().equals(user.getUsername())) {
            throw new UserException("Username already taken");
        }

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setFirstname(user.getFirstname());
        userToUpdate.setLastname(user.getLastname());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setProfilePictureBinary(user.getProfilePictureBinary());
        userToUpdate.setFriends(user.getFriends());
        userToUpdate.setChips(user.getChips());
        userToUpdate.setEnabled(user.getEnabled());

        return saveUser(userToUpdate);
    }

    @Override
    public User changePassword(User user) throws UserException {
        User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UserException("User not found"));
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return saveUser(dbUser);
    }

    @Override
    public User checkSocialUser(User user) {
        Optional<User> dbUser = userRepository.findBySocialId(user.getSocialId());

        if (!dbUser.isPresent()) {
            user.setEnabled(1);
            user = saveUser(user);
            UserRole role = new UserRole(user.getId(), "ROLE_USER");
            userRoleRepository.save(role);
            return user;
        }

        return dbUser.get();
    }

    @Override
    public User changeUserRoleToAdmin(User user) throws UserException {
        UserRole dbRole = userRoleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserException("UserRole not found"));

        dbRole.setRole("ROLE_ADMIN");
        userRoleRepository.save(dbRole);
        return user;
    }

    @Override
    public User changeUserRoleToUser(User user) throws UserException {
        UserRole dbRole = userRoleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserException("UserRole not found"));

        dbRole.setRole("ROLE_USER");
        userRoleRepository.save(dbRole);
        return user;
    }

    @Override
    public User addExperience(String id, int xp) throws UsernameNotFoundException {
        //Get data;
        User user = findUserById(id);
        user.setXpTillNext(user.getXpTillNext() + xp);

        //Do checks
        if (user.getXpTillNext() >= user.getThresholdTillNextLevel()) {
            int dif = user.getXpTillNext() - user.getThresholdTillNextLevel();
            user.setLevel(user.getLevel() + 1);
            user.setThresholdTillNextLevel((int) (user.getThresholdTillNextLevel() * 1.3));
            user.setXpTillNext(dif);
        }

        return saveUser(user);
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }
}
