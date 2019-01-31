package be.kdg.userservice.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
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

    public UserController(ResourceServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/endpointuser")
    public ResponseEntity<String> endPointUser(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> additionalInfo = tokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        return new ResponseEntity<>(String.format("Your UUID: %s, your username: %s, your role: %s", additionalInfo.get("uuid").toString(), additionalInfo.get("username").toString(),
                additionalInfo.get("role").toString()), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/endpointadmin", method = RequestMethod.GET)
    public ResponseEntity<String> endPointAdmin(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        Map<String, Object> additionalInfo = tokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
        return new ResponseEntity<String>("Your UUID: " + additionalInfo.get("uuid").toString()
                + " , your username: " + authentication.getPrincipal() + " and your role ADMIN",
                HttpStatus.OK);
    }
}
