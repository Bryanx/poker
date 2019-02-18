package be.kdg.gameservice.room.controller.dto;

import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private int id;
    @NotNull
    private String name;
    @NotNull
    private GameRules gameRules;
    @NotNull
    private List<Player> playersInRoom;
}
