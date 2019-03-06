package be.kdg.userservice;

import be.kdg.userservice.notification.model.Notification;
import be.kdg.userservice.notification.model.NotificationType;
import be.kdg.userservice.shared.TokenDto;
import be.kdg.userservice.shared.security.model.CustomUserDetails;
import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
@Transactional
public abstract class UtilTesting {
    @Value("${token.url}")
    private String TOKEN_URL;

    private static final String TESTABLE_USER_NAME1 = "josef";
    private static final String TESTABLE_USER_NAME3 = "anne";
    protected static final String TESTABLE_USER_NAME2 = "jos";
    private static final String USER_ROLE = "ROLE_USER";

    protected String testableUserId1;
    protected String testableUserId2;
    protected int testableNotificationId1;
    protected int testableNotificationId2;

    /**
     * This method will provide some test-data that will be used by the test-classes that extend from it.
     *
     * @param userRepository     The repository for users.
     * @param userRoleRepository The repository for user roles.
     */
    protected void provideTestingData(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        User testUser1 = new User();
        testUser1.setUsername(TESTABLE_USER_NAME1);
        User testUser2 = new User();
        testUser2.setUsername(TESTABLE_USER_NAME2);
        User testUser3 = new User();
        testUser3.setUsername(TESTABLE_USER_NAME3);
        testUser1.addNotification(new Notification("Test message", NotificationType.FRIEND_REQUEST, ""));
        testUser1.addNotification(new Notification("Test message", NotificationType.GAME_REQUEST, ""));
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        UserRole ur1 = new UserRole(testUser1.getId(), USER_ROLE);
        UserRole ur2 = new UserRole(testUser2.getId(), USER_ROLE);
        UserRole ur3 = new UserRole(testUser3.getId(), USER_ROLE);
        userRoleRepository.save(ur1);
        userRoleRepository.save(ur2);
        userRoleRepository.save(ur3);

        testableUserId1 = testUser1.getId();
        testableUserId2 = testUser3.getId();
        testableNotificationId1 = testUser1.getNotifications().get(0).getId();
        testableNotificationId2 = testUser1.getNotifications().get(1).getId();
    }

    /**
     * Helper method that lets us generate JWT token with only the username.
     */
    private be.kdg.userservice.shared.TokenDto getBearerToken(User user, AuthorizationServerTokenServices authorizationServerTokenServices) {
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
        return new be.kdg.userservice.shared.TokenDto(accessToken.getValue(), accessToken.getExpiresIn());
    }

    /**
     * Generic mock mvc integration test builder. Mock needs to be passed to this method because
     *
     * @param url         The API url that needs to be tested.
     * @param body        The content body that will be passed.
     * @param mock        The mock that will be used to make the api request.
     * @param requestType The type of request that has to be made.
     * @throws Exception Thrown if something goes wrong with the integration test.
     */
    protected void testMockMvc(String url,
                               String body,
                               MockMvc mock,
                               RequestType requestType,
                               User user,
                               AuthorizationServerTokenServices authorizationServerTokenServices) throws Exception {
        TokenDto tokenDto = getBearerToken(user, authorizationServerTokenServices);
        MockHttpServletRequestBuilder requestBuilder;
        ResultMatcher resultMatcher;

        switch (requestType) {
            case GET:
                requestBuilder = get("/api" + url);
                resultMatcher = status().isOk();
                break;
            case POST:
                requestBuilder = post("/api" + url);
                resultMatcher = status().isCreated();
                break;
            case PUT:
                requestBuilder = put("/api" + url);
                resultMatcher = status().isAccepted();
                break;
            case DELETE:
                requestBuilder = delete("/api" + url);
                resultMatcher = status().isAccepted();
                break;
            case PATCH:
                requestBuilder = patch("/api" + url);
                resultMatcher = status().isAccepted();
                break;
            default:
                throw new Exception("Invalid request binder.");
        }

        mock.perform(requestBuilder
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenDto.getAccess_token()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher);
    }
}
