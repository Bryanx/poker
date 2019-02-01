package be.kdg.gameservice.round.model;

import be.kdg.gameservice.card.model.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A round represents one game of poker inside the room.
 * A rounds lifecycle ends when the final card is laid on the board.
 */
public final class Round {
    /**
     * The id of the round. Used for persistence.
     */
    @Getter
    private int id;

    /**
     * The maximum number of cards that are permitted to be on the board
     * at any given time.
     */
    private static final int NUMBER_OF_CARDS_ON_BOARD = 5;

    /**
     * Current cards that are on the board.
     */
    private final Card[] cards;

    /**
     * The deck is used for shuffling all the cards at the start of a round.
     * It is also used as a holder of the remaining cards.
     *
     * @see Deck
     */
    private final Deck deck;

    /**
     * The current phase of the round. A round always starts at PRE_FLOP
     *
     * @see Phase
     */
    @Getter
    private Phase currentPhase;

    /**
     * The button represents the place of the dealer. It moves to the left every turn.
     *
     * The first player to the left of the button will be obligated to put a small blind into the pot.
     * The second player to the left will do the same, but it has to be a big blind.
     *
     * The player after that, will be the one who starts the game.
     *
     */
    @Setter
    @Getter
    private int button;

    /**
     * Determines if a round is finished.
     */
    @Setter
    @Getter
    private boolean isFinished;

    /**
     * The pot of chips. This will gradually go up as the round progresses.
     */
    @Setter
    @Getter
    private double pot;

    /**
     * The round is created with default values for all parameters.
     */
    Round() {
        this.cards = new Card[NUMBER_OF_CARDS_ON_BOARD]; //TODO: get first three cards on board (via other method).
        this.deck = new Deck();
        this.currentPhase = Phase.PRE_FLOP;

        this.button = 0; //TODO: choose random place for buttons start position.
        this.isFinished = false;
        this.pot = 0.0;
    }

    /**
     * Converts the cards-array to a list.
     * Code is written on two lines because of a bug in java with an
     * unmodifiable list in combination with converting from an array to a list.
     *
     * @return An unmodifiable list of cards that are on the board.
     */
    List<Card> getCards() {
        List<Card> cardList = Arrays.asList(cards);
        return Collections.unmodifiableList(cardList);
    }
}
