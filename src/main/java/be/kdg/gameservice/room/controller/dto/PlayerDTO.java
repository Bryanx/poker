package be.kdg.gameservice.room.controller.dto;

import be.kdg.gameservice.card.model.Card;
import be.kdg.gameservice.round.model.HandType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private int id;
    private String name;
    @NotNull
    private Card firstCard;
    @NotNull
    private Card secondCard;
    private int chipCount;
    @NotNull
    private HandType handType;
}
