package be.kdg.gameservice.round.service.impl;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.card.CardType;
import be.kdg.gameservice.round.model.HandType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class HandServiceImplTest {
    @Autowired
    HandServiceImpl handService;

    @Test
    public void determineBestPossibleHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.JACK_OF_CLUBS));
        cards.add(new Card(CardType.JACK_OF_DIAMONDS));
        cards.add(new Card(CardType.JACK_OF_SPADES));
        cards.add(new Card(CardType.ACE_OF_DIAMONDS));
        cards.add(new Card(CardType.JACK_OF_HEARTS));
        cards.add(new Card(CardType.SIX_OF_CLUBS));
        cards.add(new Card(CardType.EIGHT_OF_HEARTS));

        assertEquals(handService.determineBestPossibleHand(cards).getHandType(), HandType.FOUR_OF_A_KIND);

    }
}