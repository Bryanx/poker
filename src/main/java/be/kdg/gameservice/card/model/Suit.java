package be.kdg.gameservice.card.model;

import lombok.Getter;

/**
 * This enum holds all the possible Suits that are in a deck of cards
 */
@Getter
public enum Suit {
    CLUBS("c"),
    DIAMONDS("d"),
    HEARTS("h"),
    SPADES("s");

    /**
     * Short string representation of the suit (one letter symbol).
     */
    private final String name;

    Suit(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the suit.
     */
    @Override
    public String toString() {
        return name;
    }
}
