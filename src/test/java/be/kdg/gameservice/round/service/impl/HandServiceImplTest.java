package be.kdg.gameservice.round.service.impl;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.card.CardType;
import be.kdg.gameservice.round.model.Hand;
import be.kdg.gameservice.round.model.HandType;
import org.junit.After;
import org.junit.Before;
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

    private List<Card> cards;

    @Before
    public void setup() {
        cards = new ArrayList<>();
    }

    private void addCard(CardType cardType) {
        this.cards.add(new Card(cardType));
    }

    @Test
    public void determineHighCardHand() {
        addCard(CardType.QUEEN_OF_HEARTS);
        addCard(CardType.THREE_OF_CLUBS);
        addCard(CardType.TEN_OF_SPADES);
        addCard(CardType.EIGHT_OF_DIAMONDS);
        addCard(CardType.FIVE_OF_SPADES);
        addCard(CardType.KING_OF_CLUBS);
        addCard(CardType.SEVEN_OF_HEARTS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.HIGH_CARD);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(11, 10, 8, 6, 5)));
    }

    @Test
    public void determinePairHand() {
        addCard(CardType.FOUR_OF_CLUBS);
        addCard(CardType.THREE_OF_CLUBS);
        addCard(CardType.TEN_OF_SPADES);
        addCard(CardType.EIGHT_OF_DIAMONDS);
        addCard(CardType.KING_OF_DIAMONDS);
        addCard(CardType.TEN_OF_SPADES);
        addCard(CardType.SEVEN_OF_HEARTS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.PAIR);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(8, 8, 11, 6, 5)));
    }

    @Test
    public void determineDoublePairHand() {
        addCard(CardType.FOUR_OF_CLUBS);
        addCard(CardType.SEVEN_OF_SPADES);
        addCard(CardType.TEN_OF_SPADES);
        addCard(CardType.EIGHT_OF_DIAMONDS);
        addCard(CardType.KING_OF_DIAMONDS);
        addCard(CardType.TEN_OF_SPADES);
        addCard(CardType.SEVEN_OF_HEARTS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.TWO_PAIR);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(8, 8, 5, 5, 11)));
    }

    @Test
    public void determineThreeOfAKindHand() {
        addCard(CardType.FOUR_OF_CLUBS);
        addCard(CardType.TEN_OF_CLUBS);
        addCard(CardType.TEN_OF_SPADES);
        addCard(CardType.EIGHT_OF_DIAMONDS);
        addCard(CardType.KING_OF_DIAMONDS);
        addCard(CardType.TEN_OF_HEARTS);
        addCard(CardType.SEVEN_OF_HEARTS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.THREE_OF_A_KIND);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(8, 8, 8, 11, 6)));
    }

    @Test
    public void determineStraightHand() {
        addCard(CardType.FOUR_OF_CLUBS);
        addCard(CardType.SEVEN_OF_HEARTS);
        addCard(CardType.EIGHT_OF_HEARTS);
        addCard(CardType.FIVE_OF_DIAMONDS);
        addCard(CardType.SIX_OF_DIAMONDS);
        addCard(CardType.KING_OF_DIAMONDS);
        addCard(CardType.TEN_OF_HEARTS);

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
        addCard(CardType.TWO_OF_DIAMONDS);
        addCard(CardType.ACE_OF_DIAMONDS);
        addCard(CardType.EIGHT_OF_HEARTS);
        addCard(CardType.FOUR_OF_DIAMONDS);
        addCard(CardType.SIX_OF_DIAMONDS);
        addCard(CardType.THREE_OF_CLUBS);
        addCard(CardType.FIVE_OF_CLUBS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.STRAIGHT);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(12, 3, 2, 1, 0)));
    }

    @Test
    public void determineFlushHand() {
        addCard(CardType.FOUR_OF_CLUBS);
        addCard(CardType.SEVEN_OF_HEARTS);
        addCard(CardType.EIGHT_OF_CLUBS);
        addCard(CardType.FIVE_OF_CLUBS);
        addCard(CardType.SIX_OF_DIAMONDS);
        addCard(CardType.KING_OF_CLUBS);
        addCard(CardType.TEN_OF_CLUBS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.FLUSH);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(11, 8, 6, 3, 2)));
    }

    @Test
    public void determineFullHouseHand() {
        addCard(CardType.FOUR_OF_CLUBS); // 2
        addCard(CardType.THREE_OF_CLUBS); // 1
        addCard(CardType.FOUR_OF_DIAMONDS); // 2
        addCard(CardType.THREE_OF_SPADES); // 1
        addCard(CardType.TWO_OF_CLUBS); // 0
        addCard(CardType.FOUR_OF_SPADES); // 2
        addCard(CardType.TWO_OF_HEARTS); // 0

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.FULL_HOUSE);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(2, 2, 2, 1, 1)));
    }

    @Test
    public void determineFourOfAKindHand() {
        addCard(CardType.QUEEN_OF_HEARTS);
        addCard(CardType.QUEEN_OF_DIAMONDS);
        addCard(CardType.FOUR_OF_DIAMONDS);
        addCard(CardType.THREE_OF_CLUBS);
        addCard(CardType.QUEEN_OF_CLUBS);
        addCard(CardType.ACE_OF_SPADES);
        addCard(CardType.QUEEN_OF_SPADES);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.FOUR_OF_A_KIND);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(10, 10, 10, 10, 12)));
    }


    @Test
    public void determineStraightFlush() {
        addCard(CardType.QUEEN_OF_HEARTS);
        addCard(CardType.KING_OF_HEARTS);
        addCard(CardType.FOUR_OF_DIAMONDS);
        addCard(CardType.ACE_OF_HEARTS);
        addCard(CardType.TEN_OF_HEARTS);
        addCard(CardType.FOUR_OF_SPADES);
        addCard(CardType.JACK_OF_HEARTS);

        Hand hand = handService.determineBestPossibleHand(cards);
        assertEquals(hand.getHandType(), HandType.STRAIGHT_FLUSH);
        assertEquals(hand.getCardRankValue(), new ArrayList<>(Arrays.asList(12, 11, 10, 9, 8)));
    }
}