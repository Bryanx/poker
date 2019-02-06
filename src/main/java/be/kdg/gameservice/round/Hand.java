package be.kdg.gameservice.round;

import be.kdg.gameservice.card.model.Card;
import be.kdg.gameservice.round.model.HandType;
import lombok.Data;
import java.util.List;
import java.util.Objects;

/**
 * A hand represents the possible combinations of cards that a Player can use
 * The HandType of a player gets calculated here
 */
@Data
public class Hand implements Comparable<Hand> {

    /**
     * Used for calculating HandType based on index in string
     */
    final static String ranks = "AKQJT98765432";
    final static String suits = "hdsc";

    /**
     * List of 5 cards where handType gets calculated on
     */
    private List<Card> hand;

    private HandType type;

    public Hand(List<Card> playerCards) {
        this.hand = playerCards;
        this.determineHandType();
    }

    /**
     * Compares 2 hands based on handType
     * TODO: Use value of cards to determine ties
     * @param that
     * @return
     */
    @Override
    public int compareTo(Hand that) {
        return Integer.compare(this.type.getScore(), that.getType().getScore());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hand hand = (Hand) o;
        return type == hand.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    private HandType determineHandType() {
        // This algorithms only works for 5 cards
        if(hand.size() != 5) {
            return HandType.BAD;
        }

        int[] faceCount = new int[ranks.length()];
        long straight = 0;
        long flush = 0;

        for(Card card : this.hand) {
            int face = ranks.indexOf(card.getType().getRank().getName());

            // Non existing face detected
            if (face == -1) {
                return HandType.BAD;
            }

            straight |= (1 << face);

            faceCount[face]++;

            // Non existing suit detected
            if(!suits.contains(card.getType().getSuit().getName())) {
                return HandType.BAD;
            }
            flush |= (1 << card.getType().getSuit().getName().charAt(0));
        }

        // Shift bit pattern to rhe right as far as possible
        while(straight % 2 == 0) {
            straight >>= 1;
        }

        // Straight is 00011111; A-2-3-4-5 is 1111000000001
        boolean hasStraight = straight == 0b11111 || straight == 0b1111000000001;

        // unsets right-most 1-bit, which may be the only one set
        boolean hasFlush = (flush & (flush - 1)) == 0;

        if(hasStraight && hasFlush) {
            return HandType.STRAIGHT_FLUSH;
        }

        int total = 0;

        for(int count : faceCount) {
            if(count == 4) {
                return HandType.FOUR_OF_A_KIND;
            } if(count ==3 ) {
                total += 3;
            } else if (count == 2) {
                total += 2;
            }
        }

        if(total == 5) {
            return HandType.FULL_HOUSE;
        } else if(hasFlush) {
            return HandType.FLUSH;
        } else if(hasStraight) {
            return HandType.STRAIGHT;
        } else if(total == 3) {
            return HandType.THREE_OF_A_KIND;
        } else if(total == 4) {
            return HandType.TWO_PAIR;
        } else if(total == 2) {
            return HandType.PAIR;
        }
        return HandType.HIGH_CARD;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "type=" + type +
                ", hand= " + hand +
                '}';
    }
}
