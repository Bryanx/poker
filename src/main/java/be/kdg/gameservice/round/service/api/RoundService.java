package be.kdg.gameservice.round.service.api;

import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;

import java.util.List;
import java.util.Optional;

public interface RoundService {
    void saveAct(int roundId, String userId, ActType type, Phase phase, int bet, boolean allIn) throws RoundException;

    List<ActType> getPossibleActs(int roundId, String userId) throws RoundException;

    Round startNewRound(List<Player> playersForRound, int button);

    Player determineWinner(int roundId) throws RoundException;

    String determineNextUserId(int roundId, String userId) throws RoundException;

    Optional<Player> checkEndOfRound(int roundId) throws RoundException;

    Player distributeCoins(int roundId, Player player) throws RoundException ;

    Optional<Player> checkFolds(int roundId) throws RoundException;

    void playBlinds(Round round, int smallBlind, int bigBlind) throws RoundException;
}
