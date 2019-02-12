package be.kdg.gameservice.round.model;

import be.kdg.gameservice.room.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class represents a single act from a player on a specific round.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "act")
public final class Act {
    /**
     * The id of the act. Used for persistence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The player that was associated with the act.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player")
    private Player player;

    /**
     * The type of act.
     *
     * @see ActType
     */
    private ActType type;

    /**
     * The phase of the round that the act happened in.
     *
     * @see Phase
     */
    private Phase phase;

    /**
     * An optional bet that is officiated
     * This means that the bet can also be 0.
     */
    private int bet;

    public Act(Player player, ActType type, Phase phase, int bet) {
        this.player = player;
        this.type = type;
        this.phase = phase;
        this.bet = bet;
    }
}
