package be.kdg.gameservice;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * You can extend from this class if you want to do immutability testing in your junit tests.
 */
public abstract class ImmutabilityTesting {
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
}
