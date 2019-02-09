package be.kdg.gameservice.room.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * The rules of a game are represented in the a game format.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GameRules {
    TEXAS_HOLD_EM_EASY(4, 8, 30, 500, 6),
    TEXAS_HOLD_EM(8, 16, 25, 2500, 5),
    TEXAS_HOLD_EM_DIFFICULT(16, 32, 20, 5000, 4);

    /**
     * An obligated small bet that needs to be made.
     */
    private final int smallBlind;

    /**
     * An obligated 'big' bet that needs to be made.
     */
    private final int bigBlind;

    /**
     * The time each player has to make an act during a round.
     */
    private final int playDelay;

    /**
     * The staring chips for each player at the beginning of the round.
     */
    private final int startingChips;

    /**
     * The max number of players that are permitted in a room.
     */
    private final int maxPlayerCount;

    GameRules(int smallBlind, int bigBlind, int playDelay, int startingChips, int maxPlayerCount) {
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.playDelay = playDelay;
        this.startingChips = startingChips;
        this.maxPlayerCount = maxPlayerCount;
    }
}