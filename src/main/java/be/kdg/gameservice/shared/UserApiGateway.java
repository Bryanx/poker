package be.kdg.gameservice.shared;

import be.kdg.gameservice.room.controller.dto.UserDTO;
import be.kdg.gameservice.shared.config.WebConfig;
import be.kdg.gameservice.shared.dto.AuthDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Gateway for communicating with the user service.
 */
@Component
public class UserApiGateway {
    private final String TOKEN_URL;
    private final String USER_SERVICE_URL;
    private final RestTemplate restTemplate;

    public UserApiGateway(WebConfig webConfig, RestTemplate restTemplate) {
        this.USER_SERVICE_URL = webConfig.getUserServiceUrl();
        this.TOKEN_URL = webConfig.getTOKEN_URL();
        this.restTemplate = restTemplate;
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
     * Sends a rest template request to the user-service.
     *
     * @return The requested user based on the token.
     */
    public UserDTO getUser(String id) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(getMockToken().getAccess_token());
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(USER_SERVICE_URL + "/" + id, HttpMethod.GET, entity, UserDTO.class).getBody();
    }

    /**
     * @return A mock token from the user service.
     */
    public AuthDTO getMockToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("my-trusted-client", "secret");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.postForObject(TOKEN_URL, entity, AuthDTO.class);
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
