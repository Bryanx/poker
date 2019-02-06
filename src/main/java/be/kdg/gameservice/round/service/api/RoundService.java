package be.kdg.gameservice.round.service.api;

import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;

import java.util.List;

public interface RoundService {
    void saveAct(int roundId, int playerId, ActType type, Phase phase, int bet) throws RoundException;

    List<ActType> getPossibleActs(int roundId, int playerId) throws RoundException;

    Round startNewRound(List<Player> playersForRound, int button);

    Round getRound(int roundId) throws RoundException;

    Round saveRound(Round round);

    Player determineWinner(int roundId) throws RoundException;
}
