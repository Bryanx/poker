package be.kdg.mobile_client.model;

import lombok.Data;

@Data
public class Player {
    private int id;
    private String userId;
    private String playerName;
    private Card firstCard;
    private Card secondCard;
    private ActType lastAct;
    private boolean inRound;
    private boolean inRoom;
    private int chipCount;
    private String handType;
    private int seatNumber;
    private String access_token;
}
