package be.kdg.gameservice.round.model;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class RoundTest extends ImmutabilityTesting {
    private Round round;

    @Before
    public void setup() {
        this.round = new Round();
    }

    @Test
    public void testRoundCreation() {
        assertEquals(Phase.PRE_FLOP, this.round.getCurrentPhase());
        assertEquals(0.0, this.round.getPot(), 0);
        assertFalse(this.round.isFinished());
        assertNotEquals(null, this.round.getCards());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetCardsFail() {
        testImmutabilityCollection(round.getCards());
    }
}
