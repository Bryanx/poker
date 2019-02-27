package be.kdg.gameservice.round.model;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.room.model.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * A round represents one game of poker inside the room.
 * A rounds lifecycle ends when the final card is laid on the board.
 */
@NoArgsConstructor
@Entity
@Table(name = "round")
public final class Round {
    /**
     * The id of the round. Used for persistence.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The maximum number of cards that are permitted to be on the board
     * at any given time.
     */
    private static final int NUMBER_OF_CARDS_ON_BOARD = 5;

    /**
     * Current cards that are on the board.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "round_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Card> cards;


    /**
     * All acts that are bounded to a specific round.
     * This attribute is mostly going to be used for watching the replay.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "round_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Act> acts;

    /**
     * All the players that are participating in the round.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roud_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Player> playersInRound;

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
    private int pot;

    /**
     * The round is created with default values for all parameters.
     */
    public Round(List<Player> participatingPlayers, int button) {
        this.cards = new ArrayList<>();
        this.acts = new ArrayList<>();
        this.playersInRound = participatingPlayers;
        this.currentPhase = Phase.PRE_FLOP;
        this.button = button;
        this.isFinished = false;
        this.pot = 0;
        dealCards();
    }

    /**
     * Deals all the cards from that round to all the players that are
     * participating and the 5 cards on the board.
     */
    private void dealCards() {
        Deck deck = new Deck();
        playersInRound.forEach(player -> player.setFirstCard(deck.getCard()));
        playersInRound.forEach(player -> player.setSecondCard(deck.getCard()));

        for (int i = 0; i < NUMBER_OF_CARDS_ON_BOARD; i++) {
            cards.add(deck.getCard());
        }
    }

    /**
     * @return An unmodifiable list of cards that are on the board.
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * @return An unmodifiable list of all the participating players.
     */
    public List<Player> getPlayersInRound() {
        return Collections.unmodifiableList(playersInRound);
    }

    /**
     * @param player A player for this round0
     * @return All the other players, except the one from the argument.
     */
    public List<Player> getOtherPlayers(Player player) {
        return getPlayersInRound().stream()
                .filter(p -> p.getId() != player.getId())
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Returns all active players
     * An active player is defined as a player where his last act is not FOLD
     *
     * @return all the active players
     */
    public List<Player> getActivePlayers() {
        return getPlayersInRound().stream()
                .filter(p -> p.getLastAct() != ActType.FOLD)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * @return An unmodifiable list of all the acts from the round.
     */
    public List<Act> getActs() {
        return Collections.unmodifiableList(acts);
    }

    /**
     * Adds a newly created act to this round.
     */
    public void addAct(Act act) {
        acts.add(act);
    }

    /**
     * Removes a player from this round.
     *
     * @param player The player that needs to be removed.
     */
    public void removePlayer(Player player) {
        playersInRound.remove(player);
    }

    public void nextPhase() {
        this.currentPhase = this.currentPhase.next();
    }
}
