package be.kdg.gameservice.card;

import lombok.Getter;

/**
 * This enum holds all the possible ranks that are in a deck of cards.
 */
@Getter
public enum Rank {
    DEUCE("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("T"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    /**
     * Short string representation of the rank.
     */
    private final String name;

    Rank(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the rank.
     */
    @Override
    public String toString() {
        return name;
    }
}
