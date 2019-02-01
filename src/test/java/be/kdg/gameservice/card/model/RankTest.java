package be.kdg.gameservice.card.model;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class RankTest extends ImmutabilityTesting {
    @Test
    public void testImmutabilityGetCards() {
        testImmutabilityClass(Rank.class);
    }
}
