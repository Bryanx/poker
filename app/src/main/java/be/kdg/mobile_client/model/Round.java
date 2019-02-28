package be.kdg.mobile_client.model;

import java.util.List;

import lombok.Data;

@Data
public class Round {
    private int id;
    private List<Card> cards;
    private List<Player> playersInRound;
    private Phase currentPhase;
    private int button;
    private boolean isFinished;
    private int pot;
}
