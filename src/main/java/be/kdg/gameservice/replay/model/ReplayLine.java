package be.kdg.gameservice.replay.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * This class represents a single line of a replay.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "replay_line")
public class ReplayLine {
    /**
     * The id of the replay line
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The phase that the act was played in.
     */
    private String phase;

    /**
     * The line of the replay. A line can resemble something like:
     * "Player x played act x with x chips"
     */
    private String line;

    ReplayLine(String phase, String line) {
        this.phase = phase;
        this.line = line;
    }
}
