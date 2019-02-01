package be.kdg.gameservice.round.model;

import be.kdg.gameservice.room.model.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * This class represents a single act from a player on a specific round.
 */
@RequiredArgsConstructor
@Getter
public final class Act {
    /**
     * The id of the act. Used for persistence.
     */
    private int id;

    /**
     * The round the act was played in.
     */
    private final Round round;

    /**
     * The player that was associated with the act.
     */
    private final Player player;

    /**
     * The type of act.
     *
     * @see ActType
     */
    private final ActType type;

    /**
     * An optional bet that is officiated
     * This means that the bet can also be 0.
     */
    private final int bet;
}
