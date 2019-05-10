package be.kdg.gameservice.room.model;

import be.kdg.gameservice.round.model.Round;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A room that can be joined by player to take part
 * in rounds of poker.
 */
@NoArgsConstructor
@Entity
@Table(name = "room")
@DiscriminatorColumn(name = "type")
@DiscriminatorValue(value = "PUBLIC")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Room {
    /**
     * The id of room, used for persistence.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The name of the room.
     */
    @Getter
    @Setter
    private String name;

    /**
     * Players that are taking part in the current round of poker.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Player> playersInRoom;


    /**
     * An history of all the round that were played in the past.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Round> rounds;

    /**
     * The gameRules for this room.
     *
     * @see GameRules
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Getter
    @Setter
    private GameRules gameRules;

    /**
     * @param gameRules The game rules that apply to this room.
     * @param name      The name of this room.
     */
    public Room(GameRules gameRules, String name) {
        this.playersInRoom = new ArrayList<>();
        this.rounds = new ArrayList<>();
        this.gameRules = gameRules;
        this.name = name;
    }

    /**
     * @param name The name of this room.
     */
    public Room(String name) {
        this(new GameRules(), name);
    }

    /**
     * @return An unmodifiable list of players inside of the round.
     */
    public List<Player> getPlayersInRoom() {
        return playersInRoom;
    }

    /**
     * @return An unmodifiable list of all the played rounds.
     */
    public List<Round> getRounds() {
        return Collections.unmodifiableList(rounds);
    }

    /**
     * Adds a player to this room.
     *
     * @param player The player we need to add.
     */
    public void addPlayer(Player player) {
        playersInRoom.add(player);
    }

    /**
     * Removes a player from this room and the current round.
     *
     * @param player The player we need to remove.
     */
    public void removePlayer(Player player) {
        playersInRoom.remove(player);
        if (rounds.size() > 0) getCurrentRound().removePlayer(player);
    }

    /**
     * Adds a round to this class.
     *
     * @param round The round we need to add.
     */
    public void addRound(Round round) {
        rounds.add(round);
    }

    /**
     * @return The current round that is being played.
     */
    public Round getCurrentRound() {
        return rounds.get(rounds.size() - 1);
    }

    /**
     * Return number between 0 and maxRoomSize.
     *
     * @return available seatNumber
     */
    public int getFirstEmptySeat() {
        playersInRoom.sort(Comparator.comparingInt(Player::getSeatNumber));
        int[] occupiedSeats = playersInRoom.stream().mapToInt(Player::getSeatNumber).toArray();
        return findFirstPositiveMissingOccurrence(occupiedSeats);
    }

    /**
     * Finds the first missing positive number in an array of ints
     *
     * @param occupiedSeats The seats that are occupied.
     * @return The first positive number.
     */
    private int findFirstPositiveMissingOccurrence(int[] occupiedSeats) {
        int size = occupiedSeats.length;

        for (int i = 0; i < size; i++) {
            while (occupiedSeats[i] != i + 1) {
                if (occupiedSeats[i] <= 0 || occupiedSeats[i] >= size)
                    break;

                if (occupiedSeats[i] == occupiedSeats[occupiedSeats[i] - 1])
                    break;

                int temp = occupiedSeats[i];
                occupiedSeats[i] = occupiedSeats[temp - 1];
                occupiedSeats[temp - 1] = temp;
            }
        }

        for (int i = 0; i < size; i++) {
            if (occupiedSeats[i] != i + 1) {
                return i + 1;
            }
        }

        return size + 1;
    }
}
