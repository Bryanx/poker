package be.kdg.gameservice.card;

import lombok.Getter;

/**
 * This enum holds all the possible card combinations that
 * can be made in a deck of cards.
 */
@Getter
public enum CardType {
    TWO_OF_CLUBS(Rank.DEUCE, Suit.CLUBS, 1),
    THREE_OF_CLUBS(Rank.THREE, Suit.CLUBS, 5),
    FOUR_OF_CLUBS(Rank.FOUR, Suit.CLUBS, 9),
    FIVE_OF_CLUBS(Rank.FIVE, Suit.CLUBS, 13),
    SIX_OF_CLUBS(Rank.SIX, Suit.CLUBS, 17),
    SEVEN_OF_CLUBS(Rank.SEVEN, Suit.CLUBS, 21),
    EIGHT_OF_CLUBS(Rank.EIGHT, Suit.CLUBS, 25),
    NINE_OF_CLUBS(Rank.NINE, Suit.CLUBS, 29),
    TEN_OF_CLUBS(Rank.TEN, Suit.CLUBS, 33),
    JACK_OF_CLUBS(Rank.JACK, Suit.CLUBS, 37),
    QUEEN_OF_CLUBS(Rank.QUEEN, Suit.CLUBS, 41),
    KING_OF_CLUBS(Rank.KING, Suit.CLUBS, 45),
    ACE_OF_CLUBS(Rank.ACE, Suit.CLUBS, 49),

    TWO_OF_DIAMONDS(Rank.DEUCE, Suit.DIAMONDS, 2),
    THREE_OF_DIAMONDS(Rank.THREE, Suit.DIAMONDS, 6),
    FOUR_OF_DIAMONDS(Rank.FOUR, Suit.DIAMONDS, 10),
    FIVE_OF_DIAMONDS(Rank.FIVE, Suit.DIAMONDS, 14),
    SIX_OF_DIAMONDS(Rank.SIX, Suit.DIAMONDS, 18),
    SEVEN_OF_DIAMONDS(Rank.SEVEN, Suit.DIAMONDS, 22),
    EIGHT_OF_DIAMONDS(Rank.EIGHT, Suit.DIAMONDS, 26),
    NINE_OF_DIAMONDS(Rank.NINE, Suit.DIAMONDS, 30),
    TEN_OF_DIAMONDS(Rank.TEN, Suit.DIAMONDS, 34),
    JACK_OF_DIAMONDS(Rank.JACK, Suit.DIAMONDS, 38),
    QUEEN_OF_DIAMONDS(Rank.QUEEN, Suit.DIAMONDS, 42),
    KING_OF_DIAMONDS(Rank.KING, Suit.DIAMONDS, 46),
    ACE_OF_DIAMONDS(Rank.ACE, Suit.DIAMONDS, 50),

    TWO_OF_HEARTS(Rank.DEUCE, Suit.HEARTS, 3),
    THREE_OF_HEARTS(Rank.THREE, Suit.HEARTS, 7),
    FOUR_OF_HEARTS(Rank.FOUR, Suit.HEARTS, 11),
    FIVE_OF_HEARTS(Rank.FIVE, Suit.HEARTS, 15),
    SIX_OF_HEARTS(Rank.SIX, Suit.HEARTS, 19),
    SEVEN_OF_HEARTS(Rank.SEVEN, Suit.HEARTS, 23),
    EIGHT_OF_HEARTS(Rank.EIGHT, Suit.HEARTS, 27),
    NINE_OF_HEARTS(Rank.NINE, Suit.HEARTS, 31),
    TEN_OF_HEARTS(Rank.TEN, Suit.HEARTS, 35),
    JACK_OF_HEARTS(Rank.JACK, Suit.HEARTS, 39),
    QUEEN_OF_HEARTS(Rank.QUEEN, Suit.HEARTS, 43),
    KING_OF_HEARTS(Rank.KING, Suit.HEARTS, 47),
    ACE_OF_HEARTS(Rank.ACE, Suit.HEARTS, 51),

    TWO_OF_SPADES(Rank.DEUCE, Suit.SPADES, 4),
    THREE_OF_SPADES(Rank.THREE, Suit.SPADES, 8),
    FOUR_OF_SPADES(Rank.FOUR, Suit.SPADES, 12),
    FIVE_OF_SPADES(Rank.FIVE, Suit.SPADES, 16),
    SIX_OF_SPADES(Rank.SIX, Suit.SPADES, 20),
    SEVEN_OF_SPADES(Rank.SEVEN, Suit.SPADES,24),
    EIGHT_OF_SPADES(Rank.EIGHT, Suit.SPADES,28),
    NINE_OF_SPADES(Rank.NINE, Suit.SPADES, 32),
    TEN_OF_SPADES(Rank.TEN, Suit.SPADES, 36),
    JACK_OF_SPADES(Rank.JACK, Suit.SPADES, 40),
    QUEEN_OF_SPADES(Rank.QUEEN, Suit.SPADES, 44),
    KING_OF_SPADES(Rank.KING, Suit.SPADES, 48),
    ACE_OF_SPADES(Rank.ACE, Suit.SPADES, 52);

    /**
     * @see Rank
     */
    private final Rank rank;

    /**
     * @see Suit
     */
    private final Suit suit;

    /**
     * Sequence number of card Can be used with modulo and division to determine rank and suit of the card.
     */
    private final int evaluation;

    CardType(Rank rank, Suit suit, int evaluation) {
        this.rank = rank;
        this.suit = suit;
        this.evaluation = evaluation;
    }

    /**
     * Returns the name of the card.
     */
    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }
}
