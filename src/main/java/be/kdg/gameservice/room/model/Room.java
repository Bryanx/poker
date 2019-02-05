package be.kdg.gameservice.room.model;

import be.kdg.gameservice.round.model.Round;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A room that can be joined by player to take part
 * in rounds of poker.
 */
@Entity
@Table(name = "room")
public class Room {
    /**
     * The id of room, used for persistence.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter
    private final String name;

    /**
     * Players that are taking part in the current round of poker.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private final List<Player> playersInRound;


    /**
     * An history of all the round that were played in the past.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "round_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private final List<Round> rounds;

    /**
     * The gameRules for this room.
     *
     * @see GameRules
     */
    @Getter
    @Setter
    private GameRules gameRules;

    /**
     * @param gameRules The rules that are associated with this room.
     */
    public Room(GameRules gameRules, String name) {
        this.playersInRound = new ArrayList<>();
        this.rounds = new ArrayList<>();
        this.gameRules = gameRules;
        this.name = name;
    }

    /**
     * @return An unmodifiable list of players inside of the round.
     */
    public List<Player> getPlayersInRound() {
        return Collections.unmodifiableList(playersInRound);
    }

    /**
     * @return An unmodifiable list of all the played rounds.
     */
    public List<Round> getRounds() {
        return Collections.unmodifiableList(rounds);
    }

    /**
     * @return The current round that is being played.
     */
    public Round getCurrentRound() {
        return rounds.get(rounds.size() - 1);
    }

}
