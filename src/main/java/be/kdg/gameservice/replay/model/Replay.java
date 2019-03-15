package be.kdg.gameservice.replay.model;

import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simple class that holds a replay with all the acts that were played.
 */
@Entity
@Table(name = "replay")
public class Replay {
    /**
     * The id of the replay.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private int id;

    /**
     * The name of the room that the replay was played in.
     */
    private final String roomName;

    /**
     * The owner of this replay.
     */
    @Getter
    private final String ownerId;

    /**
     * The number of the round that the replay was played in.
     */
    private final int roundNumber;

    /**
     * All the replay lines that are present in this replay.
     *
     * @see ReplayLine
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "replay_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private final List<ReplayLine> lines;

    public Replay(String roomName, String ownerId, int roundNumber) {
        this.roomName = roomName;
        this.ownerId = ownerId;
        this.roundNumber = roundNumber;
        lines = new ArrayList<>();
    }

    /**
     * @return An unmodifiable list of all the lines in the replay.
     */
    public List<ReplayLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    /**
     * Adds a line to the replay
     *
     * @param line The line that needs to be added.
     */
    public void addLine(String line, String phase) {
        lines.add(new ReplayLine(line, phase));
    }
}
