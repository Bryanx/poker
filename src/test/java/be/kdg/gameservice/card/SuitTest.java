package be.kdg.gameservice.card;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class SuitTest extends ImmutabilityTesting {
    @Test
    public void testImmutabilityGetCards() {
        testImmutabilityClass(Suit.class);
    }
}
