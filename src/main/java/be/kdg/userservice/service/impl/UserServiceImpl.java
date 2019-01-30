package be.kdg.userservice.service.impl;

import be.kdg.userservice.model.User;
import be.kdg.userservice.persistence.UserRolesRepository;
import be.kdg.userservice.persistence.UserRepository;
import be.kdg.userservice.security.oauth2.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserDetailsService {
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
}
