package be.kdg.gameservice.shared;

import be.kdg.gameservice.room.controller.dto.UserDTO;
import be.kdg.gameservice.shared.config.WebConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Gateway for communicating with the user service.
 */
@Component
public class UserApiGateway {
    private static final String ID_KEY = "uuid";
    private final String USER_SERVICE_URL;
    private final RestTemplate restTemplate;
    private final ResourceServerTokenServices resourceTokenServices;

    public UserApiGateway(WebConfig webConfig, RestTemplate restTemplate, ResourceServerTokenServices resourceTokenServices) {
        this.USER_SERVICE_URL = webConfig.getUserServiceUrl();
        this.restTemplate = restTemplate;
        this.resourceTokenServices = resourceTokenServices;
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    public Map<String, Object> getUserInfo(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
    }

    /**
     * Sends a rest template request to the user-service to increase the user his wins.
     */
    public void addWin(String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(userId, headers);

        restTemplate.exchange(USER_SERVICE_URL + "/win", HttpMethod.POST, entity, void.class);
    }

    /**
     * Sends a rest template request to the user-service to increase the user his gamesPlayed.
     */
    public void addGamesPlayed(List<String> userIds) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<List<String>> entity = new HttpEntity<>(userIds, headers);

        restTemplate.exchange(USER_SERVICE_URL + "/gamesplayed", HttpMethod.POST, entity, void.class);
    }

    /**
     * Retries the token from the header that came in via a api request.
     *
     * @param authentication The authentication wrapper.
     * @return The token.
     */
    public String getTokenFromAuthentication(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return oAuth2AuthenticationDetails.getTokenValue();
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    public String getUserId(OAuth2Authentication authentication) {
        String token = getTokenFromAuthentication(authentication);
        return resourceTokenServices.readAccessToken(token).getAdditionalInformation().get(ID_KEY).toString();
    }

    /**
     * Sends a rest template request to the user-service.
     *
     * @param token The token used for making the request. (bearer)
     * @return The requested user based on the token.
     */
    public UserDTO getUser(String token, String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(USER_SERVICE_URL + "/" + id, HttpMethod.GET, entity, UserDTO.class).getBody();
    }

    /**
     * Sends a request to update a user using a rest template.
     *
     * @param token   The token used for making the request. (bearer)
     * @param userDto The user DTO that needs to be updated.
     * @return The updated user DTO.
     */
    public UserDTO updateUser(String token, UserDTO userDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDto, headers);

        return restTemplate.exchange(USER_SERVICE_URL, HttpMethod.PUT, entity, UserDTO.class).getBody();
    }
}
