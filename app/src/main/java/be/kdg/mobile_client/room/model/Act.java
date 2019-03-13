package be.kdg.mobile_client.room.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Act {
    private int roundId;
    private String userId;
    private int playerId;
    private int roomId;
    private ActType type;
    private Phase phase;
    private int bet;
    private int totalBet;
    private String nextUserId;
}
