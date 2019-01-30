package be.kdg.userservice.service.impl;

import be.kdg.userservice.dto.UserDto;
import be.kdg.userservice.model.Role;
import be.kdg.userservice.model.RoleType;
import be.kdg.userservice.model.User;
import be.kdg.userservice.persistence.RoleRepository;
import be.kdg.userservice.persistence.UserRepository;
import be.kdg.userservice.service.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userId);
        if(user == null){
            log.error("Invalid username or password.");
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        Set<GrantedAuthority> grantedAuthorities = getAuthorities(user);


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roleByUserId = user.getRoles();
        final Set<GrantedAuthority> authorities = roleByUserId.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString().toUpperCase())).collect(Collectors.toSet());
        return authorities;
    }

    public List<UserDto> findAll() {
        List<UserDto> users = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(user -> users.add(user.toUserDto()));
        return users;
    }

    @Override
    public User findOne(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto save(UserDto userDto) {
        User userWithDuplicateUsername = userRepository.findByUsername(userDto.getUsername());
        if(userWithDuplicateUsername != null && userDto.getId() != userWithDuplicateUsername.getId()) {
            log.error(String.format("Duplicate username %s", userDto.getUsername()));
            throw new RuntimeException("Duplicate username.");
        }
        User userWithDuplicateEmail = userRepository.findByEmail(userDto.getEmail());
        if(userWithDuplicateEmail != null && userDto.getId() != userWithDuplicateEmail.getId()) {
            log.error(String.format("Duplicate email %s", userDto.getEmail()));
            throw new RuntimeException("Duplicate email.");
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        List<RoleType> roleTypes = new ArrayList<>();
        userDto.getRole().stream().map(role -> roleTypes.add(RoleType.valueOf(role)));
        user.setRoles(roleRepository.find(userDto.getRole()));
        userRepository.save(user);
        return userDto;
    }
}
