package be.kdg.userservice.user.service.impl;

import be.kdg.userservice.shared.security.model.CustomUserDetails;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import be.kdg.userservice.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Returns a user that is loaded via its user name.
     *
     * @param username The name of the user
     * @return The user that corresponed with the username
     * @throws UsernameNotFoundException Thrown if no user was not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found in database"));

        List<String> userRoles = userRoleRepository.findRoleByUserName(username);
        LOGGER.info("Loading user with username " + username);
        return new CustomUserDetails(user, userRoles);
    }

    /**
     * Finds a user using the id.
     *
     * @param id The id of the user.
     * @return The user that corresponded with the id.
     * @throws UsernameNotFoundException Thrown if the user was not found.
     */
    @Override
    public User findUserById(String id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User was not found in the database"));
    }

    /**
     * Gives back all the users based on
     *
     * @param role The role of the user can be ROLE_USER or ROLE_ADMIN
     * @return All the users that corresponded with the role.
     * @throws UserException Thrown if a role dit not match with a user.
     */
    @Override
    public List<User> getUsers(String role) throws UserException {
        LOGGER.info("Getting all users with role " + role);
        return userRepository.findAll().stream()
                .filter(user -> userRoleRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new UserException(UserServiceImpl.class, "user was not found in role repo."))
                        .getRole().equals(role))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Returns all the users that corresponded with a name query.
     *
     * @param name The name query.
     * @return The users that corresponded with the name query.
     * @throws UserException Thrown from getUsers if error occurs there.
     */
    @Override
    public List<User> getUsersByName(String name) throws UserException {
        LOGGER.info("Getting all the users with following name query: " + name);
        return getUsers("ROLE_USER").stream()
                .filter(u -> u.getUsername().toLowerCase().contains(name.toLowerCase()))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Adds a user to the system.
     *
     * @param user The user that we need to add.
     * @return The newly created user.
     * @throws UserException Thrown if the user with that user name already exists.
     */
    @Override
    public User addUser(User user) throws UserException {
        //Check if already exists
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        if (optionalUser.isPresent()) throw new UserException(UserServiceImpl.class, "User already exists");

        //Add user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setChips(20000);
        user.setEnabled(1);
        user.setLevel(1);
        user.setThresholdTillNextLevel(100);
        user = saveUser(user);
        UserRole role = new UserRole(user.getId(), "ROLE_USER");
        userRoleRepository.save(role);

        LOGGER.info("Adding user with id " + user.getId() + " to the system");
        return user;
    }

    /**
     * Updates all the parameters of a specific user.
     *
     * @param user The user that needs to be updated.
     * @return The changed user.
     * @throws UserException Thrown if the username if already found or if the user was not found
     */
    @Override
    public User changeUser(User user) throws UserException {
        //Get data
        User userToUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(UserServiceImpl.class, "User not found"));
        Optional<User> optionalUserCheck = userRepository.findByUsername(user.getUsername());

        if (optionalUserCheck.isPresent() && !optionalUserCheck.get().getUsername().equals(user.getUsername()))
            throw new UserException(UserServiceImpl.class, "Username already taken");

        //Update information
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setFirstname(user.getFirstname());
        userToUpdate.setLastname(user.getLastname());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setProfilePictureBinary(user.getProfilePictureBinary());
        userToUpdate.setFriends(user.getFriends());
        userToUpdate.setChips(user.getChips());
        userToUpdate.setEnabled(user.getEnabled());

        LOGGER.info("Updated user with id " + user.getId());
        return saveUser(userToUpdate);
    }

    /**
     * Changes that password of a specific user.
     *
     * @param user The user that the password needs to be changed of.
     * @return The updated user.
     * @throws UserException Thrown if the user was not found in the database
     */
    @Override
    public User changePassword(User user) throws UserException {
        User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UserException(UserServiceImpl.class, "User not found"));
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        LOGGER.info("Changing password of user with id " + user.getId());
        return saveUser(dbUser);
    }

    /**
     * Checks if a user is a social user.
     *
     * @param user The user that the checks needs to happen on
     * @return The social user if present.
     */
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

        LOGGER.info("Checing social user with id " + user.getId());
        return dbUser.get();
    }

    /**
     * Changes the role of user.
     *
     * @param user The user that needs to be changed
     * @param role The new role of the user.
     * @return The updated user.
     */
    @Override
    public User changeRole(User user, String role) {
        //Get data
        UserRole dbRole = userRoleRepository.findByUserId(user.getId())
                .orElseThrow(() -> new UserException(UserServiceImpl.class, "UserRole not found"));

        //Update data
        dbRole.setRole(role);
        userRoleRepository.save(dbRole);
        LOGGER.info("Changing role of user with id " + user.getId() + " to " + role);
        return user;
    }

    /**
     * This method will keep on raising the level of the user until the xp is lower than
     * the threshold of that level.
     *
     * @param id The id of the user.
     * @param xp The xp we need to add.
     * @return The user with the new xp and/or level
     * @throws UsernameNotFoundException Thrown if the user was not found in the database.
     */
    @Override
    public synchronized User addExperience(String id, int xp) throws UsernameNotFoundException {
        //Get data;
        User user = findUserById(id);
        user.setXpTillNext(user.getXpTillNext() + xp);

        //Do checks
        while (user.getXpTillNext() >= user.getThresholdTillNextLevel()) {
            int dif = user.getXpTillNext() - user.getThresholdTillNextLevel();
            user.setXpTillNext(dif);
            user.setLevel(user.getLevel() + 1);
            user.setThresholdTillNextLevel((int) (user.getThresholdTillNextLevel() * 1.3));

            if (user.getLevel() % 10 == 0) user.setChips(user.getChips() + 10000);
            else if (user.getLevel() % 5 == 0) user.setChips(user.getChips() + 5000);
        }

        LOGGER.info("Adding " + xp + " xp to user with id " + user.getId());
        return saveUser(user);
    }

    /**
     * Add a win to the user
     *
     * @param userId The id of the user that needs to be updated.
     * @return The user with one win extra.
     */
    @Override
    public User addWin(String userId) {
        //Get data
        User user = findUserById(userId);

        //Update data
        user.setWins(user.getWins() + 1);
        LOGGER.info("Adding a win to user with id " + user.getId());
        return saveUser(user);
    }

    /**
     * Adds +1 games played to all the users.
     *
     * @param ids The ids thtat need to be opdated
     */
    @Override
    public void addGamesPlayed(List<String> ids) {
        LOGGER.info("Adding +1 game played to " + ids.size() + " users.");

        ids.forEach(id -> {
            //Get data
            User user = findUserById(id);

            //Update data
            user.setGamesPlayed(user.getGamesPlayed() + 1);
            saveUser(user);
        });
    }

    /**
     * Saves a user to the database.
     *
     * @param user The user that needs to be saved.
     * @return The saved user.
     */
    private User saveUser(User user) {
        return userRepository.save(user);
    }
}
