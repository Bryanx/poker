package be.kdg.gameservice.round.model;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.card.CardType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public final class HandTest {
    @Test
    public void testCardRankSort() {
        Hand hand = new Hand();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.JACK_OF_CLUBS));
        cards.add(new Card(CardType.ACE_OF_DIAMONDS));
        cards.add(new Card(CardType.ACE_OF_HEARTS));
        cards.add(new Card(CardType.JACK_OF_DIAMONDS));
        cards.add(new Card(CardType.JACK_OF_HEARTS));
        hand.setCards(cards);

        hand.generateCardRanks();
    }
}