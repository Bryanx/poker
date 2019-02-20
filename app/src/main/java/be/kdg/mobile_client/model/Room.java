package be.kdg.mobile_client.model;

import java.util.List;

import lombok.Data;

/**
 * A room that was retrieved by the game-service.
 */
@Data
public class Room {
    private int roomId;
    private String name;
    private GameRules gameRules;
    private List<Object> playersInRoom;
}
