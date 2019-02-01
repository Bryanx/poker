package be.kdg.gameservice.room.model;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class GameRulesTest extends ImmutabilityTesting {
    @Test
    public void testImmutabilityGameRules() {
        testImmutabilityClass(GameRules.class);
    }
}
