package be.kdg.gameservice.round.exception;

import be.kdg.gameservice.UtilTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoundExceptionTest extends UtilTesting {
    @Test
    public void testImmutabilityAttributes() {
        testImmutabilityAttributes(RoundException.class);
    }
}
