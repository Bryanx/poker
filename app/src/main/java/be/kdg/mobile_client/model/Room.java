package be.kdg.mobile_client.model;

import java.util.List;

import lombok.Data;

@Data
public class Room {
    private int roomId;
    private String name;
    private GameRules gameRules;
    private List<Object> players;
}
