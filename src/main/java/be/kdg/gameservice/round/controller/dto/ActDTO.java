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
    private int roundId;
    private int playerId;
    @NotNull
    private ActType type;
    @NotNull
    private Phase phase;
    private int bet;
}
