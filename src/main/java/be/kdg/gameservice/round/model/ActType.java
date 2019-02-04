package be.kdg.gameservice.round.model;

/**
 * This enum holds all the possible actions a player can do
 * when it is his turn.
 */
public enum ActType {
    /**
     * The ActType is undecided.
     */
    UNDECIDED,

    /**
     * Player passes this round.
     */
    FOLD,

    /**
     * Player does nothing. He play's the waiting game.
     */
    CHECK,

    /**
     * Player raises the bet.
     */
    RAISE,

    /**
     * The first time something goes into the pot.
     */
    BET,

    /**
     * Player goes with the bet or raise.
     */
    CALL
}
