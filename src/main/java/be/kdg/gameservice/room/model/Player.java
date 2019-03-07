package be.kdg.gameservice.room.model;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.HandType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * This class represents a player of a round.
 * A player can be inside a round, or it can be spectating if it has just joined the room.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "player")
public final class Player {
    /**
     * The id of the player. Used for persistence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The name of the player. The name is the same as the one used
     * in the account of the user.
     */
    private String userId;

    /**
     * The first card in the hand of the player.
     */
    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "first_card")
    private Card firstCard;

    /**
     * The second card in the hand of the player.
     */
    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "second_card")
    private Card secondCard;

    /**
     * The last act that a specific player
     */
    @Setter
    private ActType lastAct;

    /**
     * The number of chips a player has in its possession.
     */
    @Setter
    private int chipCount;

    /**
     * True if the player is participating in the current round.
     */
    @Setter
    private boolean inRound;

    /**
     * False if a player has not left the room, but has gone offline.
     */
    @Setter
    private boolean inRoom;

    /**
     * The type of hand always starts at HIGH_CARD.
     * The type changes during the round and is depended on the cards that are on the board.
     */
    @Setter
    private HandType handType;

    /**
     * The seatnumber where to player is seated on the table
     */
    @Setter
    private int seatNumber;

    @Setter
    private boolean allIn;

    /**
     * The Player is created with default values for all parameters.
     * @param chipCount The default chip count passed by the game rules.
     */
    public Player(int chipCount, String userId, int seatNumber) {
        this.chipCount = chipCount;
        this.userId = userId;
        this.inRoom = true;
        this.inRound = false;
        this.handType = HandType.BAD;
        this.lastAct = ActType.UNDECIDED;
        this.seatNumber = seatNumber;
    }

    /**
     * Resets a player to its default values.
     * This method will be called on if the current round ends and a new round is started.
     */
    public void resetPlayer() {
        this.inRound = true;
        this.handType = HandType.BAD;
        this.lastAct = ActType.UNDECIDED;
    }
}
