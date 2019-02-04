package be.kdg.gameservice.round.model;

import be.kdg.gameservice.card.model.Card;
import be.kdg.gameservice.room.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

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
     * All acts that are bounded to a specific round.
     * This attribute is mostly going to be used for watching the replay.
     */
    private final List<Act> acts;

    /**
     * All the players that are participating in the round.
     */
    private final List<Player> participatingPlayers;

    /**
     * The current phase of the round. A round always starts at PRE_FLOP
     *
     * @see Phase
     */
    @Getter
    private Phase currentPhase;

    /**
     * The button represents the place of the dealer. It moves to the left every turn.
     * <p>
     * The first player to the left of the button will be obligated to put a small blind into the pot.
     * The second player to the left will do the same, but it has to be a big blind.
     * <p>
     * The player after that, will be the one who starts the game.
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
    public Round(List<Player> participatingPlayers) {
        this.cards = new Card[NUMBER_OF_CARDS_ON_BOARD];
        this.acts = new ArrayList<>();
        this.participatingPlayers = participatingPlayers;
        this.deck = new Deck();
        this.currentPhase = Phase.PRE_FLOP;

        this.button = 0; //TODO: increment button to next place.
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

    /**
     * @return An unmodifiable list of all the participating players.
     */
    public List<Player> getParticipatingPlayers() {
        return Collections.unmodifiableList(participatingPlayers);
    }

    /**
     * @param player A player for this round0
     * @return All the other players, except the one from the argument.
     */
    public List<Player> getOtherPlayers(Player player) {
        return getParticipatingPlayers().stream()
                .filter(p -> p.getId() != player.getId())
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Adds a newly created act to this round.
     */
    public void addAct(Act act) {
        acts.add(act);
    }

    /**
     * @return An unmodifiable list of all the acts from the round.
     */
    public List<Act> getActs() {
        return Collections.unmodifiableList(acts);
    }
}
