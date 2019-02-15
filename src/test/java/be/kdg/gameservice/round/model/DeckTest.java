package be.kdg.gameservice.round.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public final class DeckTest {
    @Test
    public void cardCount() {
       assertEquals(52, new Deck().getNumberOfCards());
    }
}
