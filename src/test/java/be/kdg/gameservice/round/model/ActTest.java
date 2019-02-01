package be.kdg.gameservice.round.model;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class ActTest extends ImmutabilityTesting {
    @Test
    public void testImmutabilityAct() {
        testImmutabilityClass(Act.class);
    }
}
