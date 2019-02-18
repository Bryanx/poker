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
    SHOWDOWN {
        @Override
        public Phase next() {
            return null; // No wrap around for this enum, SHOWDOWN is the last phase of a round
        };
    };

    /**
     * Get next enum, next phase
     * @return
     */
    public Phase next() {
        // No bounds checking required here, because the last instance overrides
        return values()[ordinal() + 1];
    }
}
