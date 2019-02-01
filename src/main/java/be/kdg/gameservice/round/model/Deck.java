package be.kdg.gameservice.round.model;

import be.kdg.gameservice.card.model.Card;

import java.util.*;

/**
 * Holds all the cards that are not used on the board,
 * or for any of the players.
 */
final class Deck {
    /**
     * All cards in the deck, cannot be higher than 52.
     */
    private final Stack<Card> cards;

    /**
     * Instantiates a list of cards and fills this list with all
     * the cards from the enum Card.
     * All the cards will be shuffled when the list is created.
     *
     * @see Card
     */
    Deck() {
        this.cards = new Stack<>();
        this.cards.addAll(Arrays.asList(Card.values()));
        Collections.shuffle(cards);
    }

    /**
     * @return removes the first card from the stack.
     */
    public Card getCard() {
        return this.cards.pop();
    }

    /**
     * @return the number of cards still available in the deck.
     */
    int getNumberOfCards() {
        return cards.size();
    }
}
