package be.kdg.gameservice.round.model;

import lombok.Getter;

/**
 * All possible hand types that can be achieved when combining different cards.
 */
public enum HandType {
    /**
     * No hand type detected.
     */
    BAD(0),

    /**
     * All five cards of different rank and a variety of suits.
     * Example: Ac, Ks, 4d, 10c, 8h
     */
    HIGH_CARD(1),

    /**
     * Two cards of the same denomination and three unmatched cards.
     * 6c, 6h, 3d, 5h, Ks
     */
    PAIR(2),

    /**
     * Two sets of two cards of the same denomination and two unmatched cards.
     * Example: 4s, 4h, Jc, Jd, 9d
     */
    TWO_PAIR(2),

    /**
     * Three cards of the same denomination and two unmatched cards.
     * Example: 9c, 9h, 9d, Kh, As
     */
    THREE_OF_A_KIND(4),

    /**
     * Five cards in a sequence of any suit.
     * Example: 3h, 4d, 5c, 6h, 7d
     */
    STRAIGHT(5),

    /**
     * Five cards all of the same suit.
     * Example: 2h, 6h, 9h, Qh, Kh
     */
    FLUSH(6),

    /**
     * Three cards of one denomination and two cards of another denomination.
     * Example: Jh, Jc, Js, 7c, 7s
     */
    FULL_HOUSE(7),

    /**
     * Four cards of the same denomination, one in each suit.
     * Example: 10c, 10d, 10h, 10s, 4d
     */
    FOUR_OF_A_KIND(8),

    /**
     * Five cards in sequence, all of the same suit.
     * Example: 3s, 4s, 5s, 6s, 7s
     */
    STRAIGHT_FLUSH(9);

    @Getter
    private final int score;

    HandType(int score) {
        this.score = score;
    }
}
