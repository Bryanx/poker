package be.kdg.gameservice.round.service.impl;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.round.model.Hand;
import be.kdg.gameservice.round.model.HandType;
import be.kdg.gameservice.round.service.api.HandService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service responsible for calculating best HandType based on all combinations of 5 out of 7 cards
 */
@Service
public final class HandServiceImpl implements HandService {
    /**
     * Used for calculating HandType based on index in string
     */
    private final static String ranks = "AKQJT98765432";

    /**
     * All the suits.
     */
    private final static String suits = "hdsc";

    /**
     * Generate all subsets of 5 out of 5-7 and return best HandType out of all combinations
     * @param playerCards list of 5-7 cards
     *
     * @return The best possible hand.
     */
    @Override
    public Hand determineBestPossibleHand(List<Card> playerCards) {
        List<Set<Card>> res = new ArrayList<>();
        getSubsets(playerCards, 5, 0, new HashSet<Card>(), res);

        List<Hand> allHands = new ArrayList<>();
        for (Set<Card> handPossibility : res) {
            Hand hand = this.determineHandType(new ArrayList<>(handPossibility));
            allHands.add(hand);
        }

        Collections.sort(allHands);
        return allHands.get(allHands.size() - 1);
    }

    /**
     * Determine HandType based on list of 5 cards.
     *
     * @param hand All the cards that a player has in his hands.
     * @return The correct hand type.
     */
    private Hand determineHandType(List<Card> hand) {
        // This algorithms only works for 5 cards
        if(hand.size() != 5)
            return new Hand(HandType.BAD, hand);

        int[] faceCount = new int[ranks.length()];
        long straight = 0;
        long flush = 0;

        for(Card card : hand) {
            int face = ranks.indexOf(card.getType().getRank().getName());

            // Non existing face detected
            if (face == -1) {
                return new Hand(HandType.BAD, hand);
            }

            straight |= (1 << face);

            faceCount[face]++;

            // Non existing suit detected
            if(!suits.contains(card.getType().getSuit().getName())) {
                return new Hand(HandType.BAD, hand);
            }
            flush |= (1 << card.getType().getSuit().getName().charAt(0));
        }

        // Shift bit pattern to rhe right as far as possible
        while(straight % 2 == 0)
            straight >>= 1;

        // Straight is 00011111; A-2-3-4-5 is 1111000000001
        boolean hasStraight = straight == 0b11111 || straight == 0b1111000000001;

        // unsets right-most 1-bit, which may be the only one set
        boolean hasFlush = (flush & (flush - 1)) == 0;

        if(hasStraight && hasFlush)
            return new Hand(HandType.STRAIGHT_FLUSH, hand);

        int total = 0;

        for(int count : faceCount) {
            if(count == 4) {
                return new Hand(HandType.FOUR_OF_A_KIND, hand);
            } if(count ==3 ) {
                total += 3;
            } else if (count == 2) {
                total += 2;
            }
        }

        if(total == 5) {
            return new Hand(HandType.FULL_HOUSE, hand);
        } else if(hasFlush) {
            return new Hand(HandType.FLUSH, hand);
        } else if(hasStraight) {
            return new Hand(HandType.STRAIGHT, hand);
        } else if(total == 3) {
            return new Hand(HandType.THREE_OF_A_KIND, hand);
        } else if(total == 4) {
            return new Hand(HandType.TWO_PAIR, hand);
        } else if(total == 2) {
            return new Hand(HandType.PAIR, hand);
        }
        return new Hand(HandType.HIGH_CARD, hand);
    }

    /**
     * Creates all subsets of size k based on superSet
     *
     */
    private void getSubsets(List<Card> superSet, int k, int idx, Set<Card> current, List<Set<Card>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unsuccessful stop clause
        if (idx == superSet.size()) return;
        Card x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }
}
