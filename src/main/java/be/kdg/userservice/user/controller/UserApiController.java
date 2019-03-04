package be.kdg.userservice.user.controller;


import be.kdg.userservice.shared.security.model.CustomUserDetails;
import be.kdg.userservice.user.controller.dto.AuthDTO;
import be.kdg.userservice.user.controller.dto.SocialUserDTO;
import be.kdg.userservice.shared.TokenDto;
import be.kdg.userservice.user.controller.dto.UserDTO;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.service.api.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {
    private static final String ID_KEY = "uuid";
    private final ResourceServerTokenServices resourceTokenServices;
    private final AuthorizationServerTokenServices authorizationServerTokenServices;
    private final UserService userService;
    private final ModelMapper modelMapper;

    /**
     * Rest endpoint that returns the user based on his JWT.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUser(OAuth2Authentication authentication) {
        User user = userService.findUserById(getUserId(authentication));
        UserDTO userDto = modelMapper.map(user, UserDTO.class);

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
    public ResponseEntity<UserDTO[]> getUsers() {
        List<User> usersIn = userService.getUsers("ROLE_USER");
        UserDTO[] usersOut = modelMapper.map(usersIn, UserDTO[].class);
        return new ResponseEntity<>(usersOut, HttpStatus.OK);
    }

    /**
     * Gives back all the users.
     *
     * @return The users with a 200 status code if successful.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/admin/all")
    public ResponseEntity<UserDTO[]> getAdmins() {
        List<User> usersIn = userService.getUsers("ROLE_ADMIN");
        UserDTO[] usersOut = modelMapper.map(usersIn, UserDTO[].class);
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
    public ResponseEntity<UserDTO[]> getUsersByName(@PathVariable String name) {
        List<User> usersIn = userService.getUsersByName(name);
        UserDTO[] usersOut = modelMapper.map(usersIn, UserDTO[].class);
        return new ResponseEntity<>(usersOut, HttpStatus.OK);
    }

    /**
     * Rest endpoint that returns the user based on his JWT.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String userId) {
        User user = userService.findUserById(userId);
        UserDTO userDto = modelMapper.map(user, UserDTO.class);

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
    public ResponseEntity<UserDTO> changeUserRoleToAdmin(@PathVariable String userId) throws UserException {
        User user = userService.findUserById(userId);
        User userout = userService.changeUserRoleToAdmin(user);
        UserDTO userDto = modelMapper.map(userout, UserDTO.class);

        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * Changes user role to ROLE_USER.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/{userId}/user")
    public ResponseEntity<UserDTO> changeUserRoleToUser(@PathVariable String userId) throws UserException {
        User user = userService.findUserById(userId);
        User userout = userService.changeUserRoleToUser(user);
        UserDTO userDto = modelMapper.map(userout, UserDTO.class);

        return new ResponseEntity<>(userDto, HttpStatus.ACCEPTED);
    }

    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/user")
    public ResponseEntity<TokenDto> addUser(@Valid @RequestBody AuthDTO authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userService.addUser(userIn);

        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.CREATED);
    }

    /**
     * Changes user's enabled.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/disable")
    public ResponseEntity<UserDTO> changeEnabled(@Valid @RequestBody UserDTO userDto) throws UserException {
        User userIn = modelMapper.map(userDto, User.class);

        if (userDto.getProfilePicture() != null) {
            byte[] decodedBytes = userDto.getProfilePicture().getBytes();
            userIn.setProfilePictureBinary(decodedBytes);
        }

        User userOut = userService.changeUser(userIn);
        return new ResponseEntity<>(modelMapper.map(userOut, UserDTO.class), HttpStatus.OK);
    }

    /**
     * Rest endpoint that updates a user and returns a new JWT token with OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/user")
    public ResponseEntity<TokenDto> changeUser(@Valid @RequestBody UserDTO userDto) throws UserException {
        User userIn = modelMapper.map(userDto, User.class);

        if (userDto.getProfilePicture() != null) {
            byte[] decodedBytes = userDto.getProfilePicture().getBytes();
            userIn.setProfilePictureBinary(decodedBytes);
        }

        User userOut = userService.changeUser(userIn);
        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.OK);
    }

    /**
     * Rest endpoint that patches a user password and returns an OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/user")
    public ResponseEntity<UserDTO> changePassword(@Valid @RequestBody AuthDTO authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userService.changePassword(userIn);

        return new ResponseEntity<>(modelMapper.map(userOut, UserDTO.class), HttpStatus.OK);
    }

    /**
     * This api will add xp to the current user.
     *
     * @param xp The xp that needs to be added.
     * @param authentication The user itself
     * @return status code 202 with the updated user dto.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PatchMapping("/user/level/{xp}")
    public ResponseEntity<UserDTO> addXp(@PathVariable int xp, OAuth2Authentication authentication) {
        User user = userService.addExperience(getUserId(authentication), xp);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.ACCEPTED);
    }

    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/sociallogin")
    public ResponseEntity<TokenDto> socialLogin(@Valid @RequestBody SocialUserDTO socialUserDto) throws UserException {
        User userIn = modelMapper.map(socialUserDto, User.class);
        User userOut = userService.checkSocialUser(userIn);

        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.OK);
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    private String getUserId(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue())
                .getAdditionalInformation().get(ID_KEY).toString();
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
