package be.kdg.userservice.user.controller;


import be.kdg.userservice.shared.BaseController;
import be.kdg.userservice.shared.TokenDto;
import be.kdg.userservice.shared.security.model.CustomUserDetails;
import be.kdg.userservice.user.controller.dto.AuthDto;
import be.kdg.userservice.user.controller.dto.SocialUserDto;
import be.kdg.userservice.user.controller.dto.UserDto;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController extends BaseController {
    private final AuthorizationServerTokenServices authorizationServerTokenServices;
    private final SimpMessagingTemplate template;
    private final UserService userService;
    private final ModelMapper modelMapper;


    /**
     * Rest endpoint that returns the user based on his JWT.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(OAuth2Authentication authentication) {
        User user = userService.findUserById(getUserId(authentication));
        UserDto userDto = modelMapper.map(user, UserDto.class);

        if (user.getProfilePictureBinary() != null) {
            userDto.setProfilePicture(new String(user.getProfilePictureBinary()));
        } else {
            userDto.setProfilePicture(null);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Gives back all the users that have a user-role.
     *
     * @return The users with a 200 status code if successful.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<UserDto[]> getUsers() {
        List<User> usersIn = userService.getUsers("ROLE_USER");
        UserDto[] usersOut = modelMapper.map(usersIn, UserDto[].class);
        return new ResponseEntity<>(usersOut, HttpStatus.OK);
    }

    /**
     * Gives back all the users.
     *
     * @return The users with a 200 status code if successful.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/admin/all")
    public ResponseEntity<UserDto[]> getAdmins() {
        List<User> usersIn = userService.getUsers("ROLE_ADMIN");
        UserDto[] usersOut = modelMapper.map(usersIn, UserDto[].class);
        return new ResponseEntity<>(usersOut, HttpStatus.OK);
    }

    /**
     * This api method will search all the users for a matching string in their name.
     *
     * @param name The regex that we need to user for our search.
     * @return All the users that corresponded with the name and status code 200 if succeeded.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{name}")
    public ResponseEntity<UserDto[]> getUsersByName(@PathVariable String name) {
        List<User> usersIn = userService.getUsersByName(name);
        UserDto[] usersOut = modelMapper.map(usersIn, UserDto[].class);
        return new ResponseEntity<>(usersOut, HttpStatus.OK);
    }

    /**
     * Rest endpoint that returns the user based on his JWT.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        User user = userService.findUserById(userId);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        if (user.getProfilePictureBinary() != null) {
            userDto.setProfilePicture(new String(user.getProfilePictureBinary()));
        } else {
            userDto.setProfilePicture(null);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Changes user role to ROLE_ADMIN.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{userId}/admin")
    public ResponseEntity<UserDto> changeUserRoleToAdmin(@PathVariable String userId) throws UserException {
        User user = userService.findUserById(userId);
        User userout = userService.changeUserRoleToAdmin(user);
        UserDto userDto = modelMapper.map(userout, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * Changes user role to ROLE_USER.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{userId}/user")
    public ResponseEntity<UserDto> changeUserRoleToUser(@PathVariable String userId) throws UserException {
        User user = userService.findUserById(userId);
        User userout = userService.changeUserRoleToUser(user);
        UserDto userDto = modelMapper.map(userout, UserDto.class);

        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/user")
    public ResponseEntity<TokenDto> addUser(@Valid @RequestBody AuthDto authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userService.addUser(userIn);

        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.CREATED);
    }

    /**
     * Changes user's enabled.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/disable")
    public ResponseEntity<UserDto> changeEnabled(@Valid @RequestBody UserDto userDto) throws UserException {
        User userIn = modelMapper.map(userDto, User.class);

        if (userDto.getProfilePicture() != null) {
            byte[] decodedBytes = userDto.getProfilePicture().getBytes();
            userIn.setProfilePictureBinary(decodedBytes);
        }

        User userOut = userService.changeUser(userIn);
        return new ResponseEntity<>(modelMapper.map(userOut, UserDto.class), HttpStatus.OK);
    }

    /**
     * Rest endpoint that updates a user and returns a new JWT token with OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/user")
    public ResponseEntity<TokenDto> changeUser(@Valid @RequestBody UserDto userDto) throws UserException {
        User userIn = modelMapper.map(userDto, User.class);

        if (userDto.getProfilePicture() != null) {
            byte[] decodedBytes = userDto.getProfilePicture().getBytes();
            userIn.setProfilePictureBinary(decodedBytes);
        }

        User userOut = userService.changeUser(userIn);
        this.template.convertAndSend("/user/receive-myself/" + userDto.getId(), userOut);
        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.OK);
    }

    /**
     * Rest endpoint that patches a user password and returns an OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/user")
    public ResponseEntity<UserDto> changePassword(@Valid @RequestBody AuthDto authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userService.changePassword(userIn);
        return new ResponseEntity<>(modelMapper.map(userOut, UserDto.class), HttpStatus.OK);
    }

    /**
     * This api will add xp to the current user.
     * After that, the api will push the new user to a web socket connection.
     *
     * @param xp             The xp that needs to be added.
     * @param authentication The user itself
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/user/level/{xp}")
    public void addXp(@PathVariable int xp, OAuth2Authentication authentication) {
        User user = userService.addExperience(getUserId(authentication), xp);
        UserDto userDTO = modelMapper.map(user, UserDto.class);
        this.template.convertAndSend("/user/receive-myself/" + getUserId(authentication), userDTO);
    }

    /**
     * This api will increase the user his wins by 1.
     */
    @PostMapping("/user/win")
    public ResponseEntity addWin(@Valid @RequestBody String userId) throws UserException {
        userService.addWin(userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * This api will increase gamesPlayed of all in-game users with 1.
     */
    @PostMapping("/user/gamesplayed")
    public ResponseEntity addGamesPlayed(@Valid @RequestBody List<String> userIds) throws UserException {
        userService.addGamesPlayed(userIds);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/sociallogin")
    public ResponseEntity<TokenDto> socialLogin(@Valid @RequestBody SocialUserDto socialUserDto) throws UserException {
        User userIn = modelMapper.map(socialUserDto, User.class);
        User userOut = userService.checkSocialUser(userIn);

        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.OK);
    }

    /**
     * Helper method that lets us generate JWT token with only the username.
     */
    private TokenDto getBearerToken(User user) {
        HashMap<String, String> authorizationParameters = new HashMap<>();
        authorizationParameters.put("scope", "read");
        authorizationParameters.put("username", user.getUsername());
        authorizationParameters.put("client_id", "my-trusted-client");
        authorizationParameters.put("grant", "password");

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");

        Set<String> responseType = new HashSet<>();
        responseType.add("password");

        Set<String> scopes = new HashSet<>();
        scopes.add("read");
        scopes.add("write");

        OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, "my-trusted-client", authorities, true, scopes, null, "", responseType, null);

        CustomUserDetails userPrincipal = new CustomUserDetails(user, roles);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);

        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(authenticationRequest);
        return new TokenDto(accessToken.getValue(), accessToken.getExpiresIn());
    }
}
