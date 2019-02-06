package be.kdg.userservice.user.controller;


import be.kdg.userservice.user.dto.AuthDto;
import be.kdg.userservice.user.dto.UserDto;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.service.api.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {
    private final ResourceServerTokenServices tokenServices;
    private final UserService userServiceImpl;
    private final ModelMapper modelMapper;

    public UserController(ResourceServerTokenServices tokenServices, UserService userServiceImpl, ModelMapper modelMapper) {
        this.tokenServices = tokenServices;
        this.userServiceImpl = userServiceImpl;
        this.modelMapper = modelMapper;
    }

    /**
     * Rest endpoint that returns the user based on his JWT.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> additionalInfo = tokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        User user = userServiceImpl.findUserById(additionalInfo.get("uuid").toString());
        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }


    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/user")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody AuthDto authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userServiceImpl.addUser(userIn);

        return new ResponseEntity<>(modelMapper.map(userOut, UserDto.class), HttpStatus.CREATED);
    }


    /**
     * Rest endpoint that updates a user and returns an OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user")
    public ResponseEntity<UserDto> changeUser(@Valid @RequestBody UserDto userDto) throws UserException {
        User userIn = modelMapper.map(userDto, User.class);
        User userOut = userServiceImpl.changeUser(userIn);

        return new ResponseEntity<>(modelMapper.map(userOut, UserDto.class), HttpStatus.OK);
    }

    /**
     * Rest endpoint that patches a user password and returns an OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/user")
    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody AuthDto authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userServiceImpl.changePassword(userIn);

        return new ResponseEntity<>(modelMapper.map(userOut, UserDto.class), HttpStatus.OK);
    }
}
