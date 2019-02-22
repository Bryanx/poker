package be.kdg.userservice;


import be.kdg.userservice.user.model.User;
import be.kdg.userservice.user.model.UserRole;
import be.kdg.userservice.user.persistence.UserRepository;
import be.kdg.userservice.user.persistence.UserRoleRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
public abstract class UtilTesting {
    //private static final String TOKEN_URL = "https://poker-user-service.herokuapp.com/oauth/token?grant_type=password&username=remismeets&password=12345";
    private static final String TOKEN_URL = "http://localhost:5000/oauth/token?grant_type=password&username=remismeets&password=12345";
    private static final String TESTABLE_USER_NAME1 = "josef";
    private static final String TESTABLE_USER_NAME3 = "anne";
    protected static final String TESTABLE_USER_NAME2 = "jos";
    private static final String USER_ROLE = "ROLE_USER";

    protected String testableUserId1;
    protected String testableUserId2;

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
    protected void testMockMvc(String url, String body, MockMvc mock, RequestType requestType) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth("my-trusted-client", "secret");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        TokenDto tokenDto = restTemplate.postForObject(TOKEN_URL, entity, TokenDto.class);

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


        assert tokenDto != null;
        mock.perform(requestBuilder
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", tokenDto.getAccess_token()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher);
    }
}
