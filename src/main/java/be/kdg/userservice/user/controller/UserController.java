package be.kdg.userservice.user.controller;


import be.kdg.userservice.user.dto.UserDto;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<UserDto> endPointUser(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> additionalInfo = tokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        User user = userServiceImpl.findUserById(additionalInfo.get("uuid").toString());
        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }
}
