package be.kdg.gameservice.round.controller.dto;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.round.model.Phase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundDTO {
    private int id;
    @NotNull
    private List<Card> cards;
    @NotNull
    private List<Player> playersInRound;
    @NotNull
    private Phase currentPhase;
    private int button;
    private int smallBlind;
    private int bigBlind;
    private boolean isFinished;
    private int pot;
}
