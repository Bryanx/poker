package be.kdg.mobile_client.model;

import lombok.Data;

@Data
public class GameRules {
    private int smallBlind;
    private int bigBlind;
    private int playDelay;
    private int startingChips;
    private int maxPlayerCount;
}
