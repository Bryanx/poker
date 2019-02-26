package be.kdg.gameservice.room.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class that holds a player that is on the white list of people that can join a
 * specific private room.
 */
@NoArgsConstructor
@Entity
@Table(name = "white_listed_player")
@Getter
public final class WhiteListedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userId;

    public WhiteListedUser(String userId) {
        this.userId = userId;
    }
}
