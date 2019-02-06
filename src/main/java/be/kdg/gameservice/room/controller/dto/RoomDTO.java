package be.kdg.gameservice.room.controller.dto;

import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.round.model.Round;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private int roomId;
    @NotNull
    private String name;
    @NotNull
    private GameRules gameRules;
    @NotNull
    private Round currentRound;
}
