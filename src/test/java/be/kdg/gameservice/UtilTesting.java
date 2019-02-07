package be.kdg.gameservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
     * TODO: write documentation
     *
     * @param url
     * @throws Exception
     */
    protected void testMockMvcGet(String url, MockMvc mock) throws Exception {
        mock.perform(get("/api" + url)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    /**
     * TODO: write documentation
     *
     * @param url
     * @throws Exception
     */
    protected void testMockMvcPost(String url, String json, MockMvc mock) throws Exception {
        if (json != null) {
            mock.perform(post("/api" + url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        } else {
            mock.perform(post("/api" + url)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isCreated());
        }
    }
}
