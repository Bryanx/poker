package be.kdg.userservice.user.service.impl;

import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.persistence.UserRolesRepository;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.security.model.CustomUserDetails;
import be.kdg.userservice.user.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRolesRepository userRolesRepository) {
        this.userRepository = userRepository;
        this.userRolesRepository = userRolesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (null == user) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        List<String> userRoles = userRolesRepository.findRoleByUserName(userName);
        return new CustomUserDetails(user, userRoles);
    }

    @Override
    public User findUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Bad credentials");
        } else {
            return user.get();
        }
    }
}
