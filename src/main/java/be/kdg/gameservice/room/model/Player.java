package be.kdg.gameservice.room.model;

import be.kdg.gameservice.card.model.Card;
import be.kdg.gameservice.round.model.HandType;
import lombok.Getter;
import lombok.Setter;

/**
 * This class represents a player of a round.
 * A player can be inside a round, or it can be spectating if it has just joined the room.
 */
public final class Player {
    /**
     * The id of the player. Used for persistence.
     */
    @Getter
    private int id;

    /**
     * The first card in the hand of the player.
     */
    @Setter
    @Getter
    private Card firstCard;

    /**
     * The second card in the hand of the player.
     */
    @Setter
    @Getter
    private Card secondCard;

    /**
     * The number of chips a player has in its possession.
     */
    @Setter
    @Getter
    private int chipCount;

    /**
     * True if it is this players turn.
     */
    @Setter
    @Getter
    private boolean inPlay;

    /**
     * True if the player is participating in the current round.
     */
    @Getter
    @Setter
    private boolean inRound;

    /**
     * False if a player has not left the room, but has gone offline.
     */
    @Setter
    @Getter
    private boolean isActive;

    /**
     * The type of hand always starts at HIGH_CARD.
     * The type changes during the round and is depended on the cards that are on the board.
     */
    @Setter
    @Getter
    private HandType handType;

    /**
     * The Player is created with default values for all parameters.
     * @param chipCount The default chip count passed by the game rules.
     */
    public Player(int chipCount) {
        this.chipCount = chipCount;
        this.inPlay = false;
        this.isActive = true;
        this.handType = HandType.BAD;
    }

    /**
     * Resets a player to its default values.
     * This method will be called on if the current round ends and a new round is started.
     */
    public void resetPlayer() {
        this.inPlay = false;
        this.handType = HandType.BAD;
    }
}
