package be.kdg.gameservice.room.model;

import lombok.Getter;

/**
 * The rules of a game are represented in the a game format.
 */
@Getter
public enum GameRules {
    /**
     * The default settings.
     */
    TEXAS_HOLD_EM(4, 8, 30, 500, 6);

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