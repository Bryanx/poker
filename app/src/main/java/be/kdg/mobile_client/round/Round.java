package be.kdg.mobile_client.round;

import java.util.List;

import be.kdg.mobile_client.room.model.Card;
import be.kdg.mobile_client.room.model.Phase;
import be.kdg.mobile_client.room.model.Player;
import lombok.Data;

@Data
public class Round {
    private int id;
    private List<Card> cards;
    private List<Player> playersInRound;
    private Phase currentPhase;
    private int button;
    private boolean finished;
    private int pot;
    private int bigBlind;
    private int smallBlind;
}
