package be.kdg.gameservice;

import be.kdg.gameservice.shared.TokenDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
public abstract class UtilTesting {
    private static final String TOKEN_URL = "http://localhost:5000/oauth/token?grant_type=password&username=remismeets&password=12345";

    /**
     * Tests the immutability of a class.
     * If you are working with an enum, the enum will automatically be recognized as final.
     *
     * @param aClass The class you want to test.
     */
    protected void testImmutabilityAttributes(Class aClass) {
        Arrays.stream(aClass.getDeclaredFields())
                .filter(f -> !f.getName().equalsIgnoreCase("id"))
                .forEach(f -> assertTrue(Modifier.isFinal(f.getModifiers())));
    }

    /**
     * Tests the immutability of a given collection.
     *
     * @param col The collection you want to test.
     */
    protected void testImmutabilityCollection(List col) {
        col.remove(0);
        col.clear();
        fail("Collection should be immutable");
    }

    /**
     * Generic mock mvc integration test builder. Mock needs to be passed to this method because
     *
     * @param url The API url that needs to be tested.
     * @param body The content body that will be passed.
     * @param mock The mock that will be used to make the api request.
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
