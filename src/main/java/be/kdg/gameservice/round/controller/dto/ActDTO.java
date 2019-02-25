package be.kdg.gameservice.round.controller.dto;

import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActDTO {
    private String userId;
    private int roundId;
    private int playerId;
    private int roomId;
    private ActType type;
    private Phase phase;
    private int bet;
    private int totalBet;
    private String nextUserId;

    public ActDTO(String userId, int roundId, ActType type, Phase phase, int bet) {
        this.userId = userId;
        this.roundId = roundId;
        this.type = type;
        this.phase = phase;
        this.bet = bet;
    }
}
