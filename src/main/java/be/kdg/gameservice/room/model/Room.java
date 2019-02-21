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
public class Room {
    /**
     * The id of room, used for persistence.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter
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
    @Getter
    @Setter
    private GameRules gameRules;

    /**
     * @param gameRules The rules that are associated with this room.
     */
    public Room(GameRules gameRules, String name) {
        this.playersInRoom = new ArrayList<>();
        this.rounds = new ArrayList<>();
        this.gameRules = gameRules;
        this.name = name;
    }

    /**
     * @return An unmodifiable list of players inside of the round.
     */
    public List<Player> getPlayersInRoom() {
        return Collections.unmodifiableList(playersInRoom);
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
     * @return available seatNumber
     */
    public int getFirstEmptySeat() {
//        playersInRoom.sort(Comparator.comparingInt(Player::getSeatNumber));
//
//        for (int i = 0; i < playersInRoom.size(); i++) {
//            if (playersInRoom.get(i).getSeatNumber() != i) {
//                return i;
//            }
//        }
//
//        return 0;

        int[] A = playersInRoom.stream().mapToInt(Player::getSeatNumber).toArray();

        int n = A.length;

        for (int i = 0; i < n; i++) {
            while (A[i] != i + 1) {
                if (A[i] <= 0 || A[i] >= n)
                    break;

                if(A[i]==A[A[i]-1])
                    break;

                int temp = A[i];
                A[i] = A[temp - 1];
                A[temp - 1] = temp;
            }
        }

        for (int i = 0; i < n; i++){
            if (A[i] != i + 1){
                return i + 1;
            }
        }

        return n + 1;
    }
}
