package be.kdg.gameservice.round.service.impl;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.card.CardType;
import be.kdg.gameservice.round.model.Hand;
import be.kdg.gameservice.round.model.HandType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class HandServiceImplTest {
    @Autowired
    HandServiceImpl handService;

    @Test
    public void determineHighCardHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.QUEEN_OF_HEARTS));
        cards.add(new Card(CardType.THREE_OF_CLUBS));
        cards.add(new Card(CardType.TEN_OF_SPADES));
        cards.add(new Card(CardType.EIGHT_OF_DIAMONDS));
        cards.add(new Card(CardType.FIVE_OF_SPADES));
        cards.add(new Card(CardType.KING_OF_CLUBS));
        cards.add(new Card(CardType.SEVEN_OF_HEARTS));

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.HIGH_CARD);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(11, 10, 8, 6, 5)));
    }

    @Test
    public void determinePairHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.FOUR_OF_CLUBS));
        cards.add(new Card(CardType.THREE_OF_CLUBS));
        cards.add(new Card(CardType.TEN_OF_SPADES));
        cards.add(new Card(CardType.EIGHT_OF_DIAMONDS));
        cards.add(new Card(CardType.KING_OF_DIAMONDS));
        cards.add(new Card(CardType.TEN_OF_SPADES));
        cards.add(new Card(CardType.SEVEN_OF_HEARTS));

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.PAIR);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(8, 8, 11, 6, 5)));
    }

    @Test
    public void determineDoublePairHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.FOUR_OF_CLUBS));
        cards.add(new Card(CardType.SEVEN_OF_SPADES));
        cards.add(new Card(CardType.TEN_OF_SPADES));
        cards.add(new Card(CardType.EIGHT_OF_DIAMONDS));
        cards.add(new Card(CardType.KING_OF_DIAMONDS));
        cards.add(new Card(CardType.TEN_OF_SPADES));
        cards.add(new Card(CardType.SEVEN_OF_HEARTS));

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.TWO_PAIR);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(8, 8, 5, 5, 11)));
    }

    @Test
    public void determineThreeOfAKindHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.FOUR_OF_CLUBS));
        cards.add(new Card(CardType.TEN_OF_CLUBS));
        cards.add(new Card(CardType.TEN_OF_SPADES));
        cards.add(new Card(CardType.EIGHT_OF_DIAMONDS));
        cards.add(new Card(CardType.KING_OF_DIAMONDS));
        cards.add(new Card(CardType.TEN_OF_HEARTS));
        cards.add(new Card(CardType.SEVEN_OF_HEARTS));

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.THREE_OF_A_KIND);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(8, 8, 8, 11, 6)));
    }

    @Test
    public void determineStraightHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.FOUR_OF_CLUBS));
        cards.add(new Card(CardType.SEVEN_OF_HEARTS));
        cards.add(new Card(CardType.EIGHT_OF_HEARTS));
        cards.add(new Card(CardType.FIVE_OF_DIAMONDS));
        cards.add(new Card(CardType.SIX_OF_DIAMONDS));
        cards.add(new Card(CardType.KING_OF_DIAMONDS));
        cards.add(new Card(CardType.TEN_OF_HEARTS));


        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.STRAIGHT);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(6, 5, 4, 3, 2)));
    }

    /**
     * Small variation on above determineStraightHand
     * This will detect A 2 3 4 5 (an edge case)
     */
    @Test
    public void determineStraightAceToFiveHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.TWO_OF_DIAMONDS));
        cards.add(new Card(CardType.ACE_OF_DIAMONDS));
        cards.add(new Card(CardType.EIGHT_OF_HEARTS));
        cards.add(new Card(CardType.FOUR_OF_DIAMONDS));
        cards.add(new Card(CardType.SIX_OF_DIAMONDS));
        cards.add(new Card(CardType.THREE_OF_CLUBS));
        cards.add(new Card(CardType.FIVE_OF_CLUBS));


        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.STRAIGHT);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(12, 3, 2, 1, 0)));
    }

    @Test
    public void determineFlushHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.FOUR_OF_CLUBS));
        cards.add(new Card(CardType.SEVEN_OF_HEARTS));
        cards.add(new Card(CardType.EIGHT_OF_CLUBS));
        cards.add(new Card(CardType.FIVE_OF_CLUBS));
        cards.add(new Card(CardType.SIX_OF_DIAMONDS));
        cards.add(new Card(CardType.KING_OF_CLUBS));
        cards.add(new Card(CardType.TEN_OF_CLUBS));


        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.FLUSH);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(11, 8, 6, 3, 2)));
    }

    @Test
    public void determineFullHouseHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.FOUR_OF_CLUBS)); // 2
        cards.add(new Card(CardType.THREE_OF_CLUBS)); // 1
        cards.add(new Card(CardType.FOUR_OF_DIAMONDS)); // 2
        cards.add(new Card(CardType.THREE_OF_SPADES)); // 1
        cards.add(new Card(CardType.TWO_OF_CLUBS)); // 0
        cards.add(new Card(CardType.FOUR_OF_SPADES)); // 2
        cards.add(new Card(CardType.TWO_OF_HEARTS)); // 0


        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.FULL_HOUSE);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(2, 2, 2, 1, 1)));
    }

    @Test
    public void determineFourOfAKindHand() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.QUEEN_OF_HEARTS));
        cards.add(new Card(CardType.QUEEN_OF_DIAMONDS));
        cards.add(new Card(CardType.FOUR_OF_DIAMONDS));
        cards.add(new Card(CardType.THREE_OF_CLUBS));
        cards.add(new Card(CardType.QUEEN_OF_CLUBS));
        cards.add(new Card(CardType.ACE_OF_SPADES));
        cards.add(new Card(CardType.QUEEN_OF_SPADES));


        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.FOUR_OF_A_KIND);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(10, 10, 10, 10, 12)));
    }


    @Test
    public void determineStraightFlush() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(CardType.QUEEN_OF_HEARTS));
        cards.add(new Card(CardType.KING_OF_HEARTS));
        cards.add(new Card(CardType.FOUR_OF_DIAMONDS));
        cards.add(new Card(CardType.ACE_OF_HEARTS));
        cards.add(new Card(CardType.TEN_OF_HEARTS));
        cards.add(new Card(CardType.FOUR_OF_SPADES));
        cards.add(new Card(CardType.JACK_OF_HEARTS));


        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.STRAIGHT_FLUSH);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(12, 11, 10, 9, 8)));
    }
}