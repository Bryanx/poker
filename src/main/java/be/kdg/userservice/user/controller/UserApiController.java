package be.kdg.userservice.user.controller;


import be.kdg.userservice.security.model.CustomUserDetails;
import be.kdg.userservice.user.dto.AuthDto;
import be.kdg.userservice.user.dto.SocialUserDto;
import be.kdg.userservice.user.dto.TokenDto;
import be.kdg.userservice.user.dto.UserDto;
import be.kdg.userservice.user.exception.UserException;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.service.api.UserService;
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
public class UserApiController {
    private final ResourceServerTokenServices resourceTokenServices;
    private final AuthorizationServerTokenServices authorizationServerTokenServices;
    private final UserService userServiceImpl;
    private final ModelMapper modelMapper;

    public UserApiController(ResourceServerTokenServices resourceTokenServices, AuthorizationServerTokenServices authorizationServerTokenServices, UserService userServiceImpl, ModelMapper modelMapper) {
        this.resourceTokenServices = resourceTokenServices;
        this.authorizationServerTokenServices = authorizationServerTokenServices;
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
        Map<String, Object> additionalInfo = resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        User user = userServiceImpl.findUserById(additionalInfo.get("uuid").toString());
        UserDto userDto = modelMapper.map(user, UserDto.class);

        if (user.getProfilePictureBinary() != null) {
            userDto.setProfilePicture(new String(user.getProfilePictureBinary()));
        } else {
            userDto.setProfilePicture(null);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Rest endpoint that returns the user based on his JWT.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
        User user = userServiceImpl.findUserById(userId);
        UserDto userDto = modelMapper.map(user, UserDto.class);

        if (user.getProfilePictureBinary() != null) {
            userDto.setProfilePicture(new String(user.getProfilePictureBinary()));
        } else {
            userDto.setProfilePicture(null);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/user")
    public ResponseEntity<TokenDto> addUser(@Valid @RequestBody AuthDto authDto) throws UserException {
        User userIn = modelMapper.map(authDto, User.class);
        User userOut = userServiceImpl.addUser(userIn);

        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.CREATED);
    }

    /**
     * Rest endpoint that updates a user and returns a new JWT token with OK status code.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user")
    public ResponseEntity<TokenDto> changeUser(@Valid @RequestBody UserDto userDto) throws UserException {
        User userIn = modelMapper.map(userDto, User.class);

        if (userDto.getProfilePicture() != null) {
            byte[] decodedBytes = userDto.getProfilePicture().getBytes();
            userIn.setProfilePictureBinary(decodedBytes);
        }

        User userOut = userServiceImpl.changeUser(userIn);
        return new ResponseEntity<>(getBearerToken(userOut), HttpStatus.OK);
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

    /**
     * Rest endpoint that creates a user and returns a CREATED status code.
     */
    @PostMapping("/sociallogin")
    public ResponseEntity<TokenDto> socialLogin(@Valid @RequestBody SocialUserDto socialUserDto) throws UserException {
        User userIn = modelMapper.map(socialUserDto, User.class);
        User userOut = userServiceImpl.checkSocialUser(userIn);

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

        OAuth2Request authorizationRequest = new OAuth2Request(authorizationParameters, "my-trusted-client", authorities, true,scopes, null, "", responseType, null);

        CustomUserDetails userPrincipal = new CustomUserDetails(user, roles);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);

        OAuth2Authentication authenticationRequest = new OAuth2Authentication(authorizationRequest, authenticationToken);
        authenticationRequest.setAuthenticated(true);

        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(authenticationRequest);
        return new TokenDto(accessToken.getValue(), accessToken.getExpiresIn());
    }
}
