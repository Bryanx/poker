package be.kdg.gameservice.round.model;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.round.service.util.SortComparator;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class determines which hand type a specific player possesses.
 */
@Data
@NoArgsConstructor
public final class Hand implements Comparable<Hand> {
    /**
     * All the card ranks.
     */
    private final static String ranks = "23456789TJQKA";

    /**
     * The determined hand type by the algorithm.
     */
    private HandType handType;

    private List<Card> cards;

    private List<Integer> cardRankValue;

    public Hand(HandType handType, List<Card> cards) {
        this.cards = cards;
        this.handType = handType;
        this.generateCardRanks();
    }

    /**
     * Will sort the card rank values on frequency and then on value
     * For example: Full house K A K K A will be sorted as K K K A A
     * Second example: Double pair T 3 3 T A will be sorted as T T 3 3 A
     *
     * @param cards The cards that need to be sorted.
     */
    private void sortCards(List<Integer> cards) {
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> outputArray = new ArrayList<>();

        // Assign elements and their count in the list and map
        cards.forEach(current -> {
            int count = map.getOrDefault(current, 0);
            map.put(current, count + 1);
            outputArray.add(current);
        });

        // Compare the map by value
        SortComparator comp = new SortComparator(map);

        // Sort the list based on custom comparator
        outputArray.sort(comp);

        cardRankValue = outputArray;
    }

    /**
     * Generate card rank values based on 5 player cards
     * Integer value of the rank will be calculated based on index in ranks string
     */
    void generateCardRanks() {
        List<String> cardRanks = cards.stream().map(c -> c.type.getRank().getName()).collect(Collectors.toList());
        cardRankValue = cardRanks.stream().map(ranks::indexOf).collect(Collectors.toList());
        this.sortCards(cardRankValue);
    }

    /**
     * Compares 2 hands with each other
     * Can be used to calculate best hand when 2 people have the same HandType
     * First we compare the HandType, if the HandType is the same we compare the rank values of the cards
     * Example: 2 Full Houses: K K K T T and K K K 3 3
     * In the above case
     *
     * @param that The hand we need to compere
     * @return The return value of the comparison.
     */
    @Override
    public int compareTo(Hand that) {
        // First compare the hands
        int handTypeCompare = Integer.compare(this.handType.getScore(), that.handType.getScore());

        if (handTypeCompare == 0 && this.cards.size() == that.cards.size()) {
            // Compare all the cards in sequential order
            // When we find 2 different rank values we know one of the hands is better than the other one
            for (int i = 0; i < this.cardRankValue.size(); i++) {
                int cardCompare = this.cardRankValue.get(i).compareTo(that.cardRankValue.get(i));
                if (cardCompare != 0) {
                    return cardCompare;
                }
            }
            return 0;
        }
        // Return if the handTypes are different
        return handTypeCompare;
    }
}
