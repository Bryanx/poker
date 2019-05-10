package be.kdg.mobile_client.room.model;

import lombok.Getter;

/**
 * The game rules that are applied to a specific room.
 */
@Getter
public class GameRules {
    private int smallBlind;
    private int bigBlind;
    private int playDelay;
    private int startingChips;
    private int maxPlayerCount;
    private int minLevel;
    private int maxLevel;
}
