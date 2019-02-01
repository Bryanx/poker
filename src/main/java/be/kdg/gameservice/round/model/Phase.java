package be.kdg.gameservice.round.model;

/**
 * Different phases the round of poker can be in.
 */
public enum Phase {
    /**
     * Phase before flop, first betting round.
     */
    PRE_FLOP,

    /**
     * First three cards are shown, second betting round.
     */
    FLOP,

    /**
     * Fourth card is shown, third betting round.
     */
    TURN,

    /**
     * Fifth and final card is shown, fourth betting round.
     */
    RIVER,

    /**
     * Last betting round is done and player show cards.
     */
    SHOWDOWN
}
