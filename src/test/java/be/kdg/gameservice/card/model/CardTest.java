package be.kdg.gameservice.card.model;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public final class CardTest extends ImmutabilityTesting {
    private List<Card> cards;

    @Before
    public void setup() {
        this.cards = new ArrayList<>(Arrays.asList(Card.values()));
    }

    @Test
    public void testCardRank() {
        for (Rank rank : Rank.values()) {
            assertEquals(4, cards.stream().filter(c -> c.getRank().equals(rank)).count());
        }
    }

    @Test
    public void testCardSuit() {
        for (Suit suit : Suit.values()) {
            assertEquals(13, cards.stream().filter(c -> c.getSuit().equals(suit)).count());
        }
    }

    @Test
    public void testCardEvaluation() {
        for (int i = 1; i <= 52; i++) {
            final int eval = i;
            assertEquals(1, cards.stream().filter(c -> c.getEvaluation() == eval).count());
        }
    }

    @Test
    public void testImmutabilityGetCards() {
        testImmutabilityClass(Card.class);
    }
}
