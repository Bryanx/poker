package be.kdg.gameservice.card;

import be.kdg.gameservice.UtilTesting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public final class CardTypeTest extends UtilTesting {
    private List<CardType> cards;

    @Before
    public void setup() {
        this.cards = new ArrayList<>(Arrays.asList(CardType.values()));
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
    public void testImmutability() {
        testImmutabilityClass(CardType.class);
    }
}
