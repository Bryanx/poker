package be.kdg.gameservice.room.model;

import be.kdg.gameservice.round.model.Round;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A room that can be joined by player to take part
 * in playedRounds of poker.
 */
public class Room {
    /**
     * The id of room, used for persistence.
     */
    @Getter
    private int id;

    /**
     * Players that are taking part in the current round of poker.
     */
    private final List<Player> playersInRound;


    /**
     * An history of all the round that were played in the past.
     */
    private final List<Round> playedRounds;

    /**
     * The current round that is being played.
     */
    private Round currentRound;

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
    public Room(GameRules gameRules) {
        this.playersInRound = new ArrayList<>();
        this.playedRounds = new ArrayList<>();
        this.gameRules = gameRules;
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
    public List<Round> getPlayedRounds() {
        return Collections.unmodifiableList(playedRounds);
    }

    /**
     * //TODO: write documentation.
     */
    public void startNewRound() {
        //TODO: implement.
    }
}
