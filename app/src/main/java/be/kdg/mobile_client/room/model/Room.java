package be.kdg.mobile_client.room.model;

import java.util.List;

import be.kdg.mobile_client.room.model.GameRules;
import be.kdg.mobile_client.room.model.Player;
import lombok.Data;

/**
 * A room that was retrieved by the game-service.
 */
@Data
public class Room {
    private int id;
    private String name;
    private GameRules gameRules;
    private List<Player> playersInRoom;
}
