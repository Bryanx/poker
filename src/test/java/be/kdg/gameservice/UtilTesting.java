package be.kdg.gameservice;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
public abstract class UtilTesting {
    /**
     * Tests the immutability of a class.
     * If you are working with an enum, the enum will automatically be recognized as final.
     *
     * @param aClass The class you want to test.
     */
    protected void testImmutabilityClass(Class aClass) {
        Arrays.stream(aClass.getDeclaredFields())
                .filter(f -> !f.getName().equalsIgnoreCase("id"))
                .forEach(f -> assertTrue(Modifier.isFinal(f.getModifiers())));

        assertTrue(Modifier.isFinal(aClass.getModifiers()));
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
     * @throws Exception Thrown if something goes wrong with the integration test.
     */
    protected void testMockMvc(String url, String body, MockMvc mock, RequestType requestType) throws Exception {
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
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(resultMatcher);
    }
}
