package be.kdg.gameservice.replay.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * This class represents a single line of a replay.
 */
@RequiredArgsConstructor
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
     * The line of the replay. A line can resemble something like:
     * "Player x played act x with x chips"
     */
    private final String line;
}
