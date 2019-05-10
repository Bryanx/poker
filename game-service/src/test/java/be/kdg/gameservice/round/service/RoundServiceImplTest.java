package be.kdg.gameservice.round.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.PlayerService;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.persistence.RoundRepository;
import be.kdg.gameservice.round.service.api.RoundService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RoundServiceImplTest extends UtilTesting {
    @Autowired
    private RoundService roundService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private RoomService roomService;

    @Before
    public void setup() {
        provideTestDataRound(roundRepository);
    }

    @Test
    public void startNewRound() {
        Round round = roundService.startNewRound(new ArrayList<>(), 2);
        assertEquals(0, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(0, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());
    }

    @Test(expected = RoundException.class)
    public void saveActFail() throws RoundException {
        roundService.saveAct(testableRoundIdWithPlayers, testableUserId, ActType.RAISE, Phase.PRE_FLOP, 25, false);
        fail("Act should not be possible for this player at this time in the round.");
    }

    @Test
    public void getPossibleActs() throws RoundException {
        List<ActType> possibleActs = roundService.getPossibleActs(testableRoundIdWithPlayers);
        assertEquals(3, possibleActs.size());
        assertTrue(possibleActs.contains(ActType.BET)
                && possibleActs.contains(ActType.CHECK)
                && possibleActs.contains(ActType.FOLD));
    }

    @Test
    public void playRoundWithCheckAndSBBB() throws RoomException, RoundException {
        Room room = roomService.addRoom("Test room", new GameRules());

        Player player1 = new Player(2500, "Maarten", 0);
        Player player2 = new Player(2500, "Remi", 1);
        Player player3 = new Player(2500, "Dirk", 2);

        playerService.joinRoom(room.getId(), player1.getUserId());
        playerService.joinRoom(room.getId(), player2.getUserId());
        playerService.joinRoom(room.getId(), player3.getUserId());

        Round round = roomService.startNewRoundForRoom(room.getId());

        assertEquals(30, round.getPot());
        assertEquals(2, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CALL, round.getCurrentPhase(), 20, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 10, false);

        assertEquals(4, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());

        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(7, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(10, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(60, round.getPot());
        assertEquals(13, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());
    }

    @Test
    public void playRoundWithFold() throws RoomException, RoundException {
        Room room = roomService.addRoom("Test room", new GameRules());

        Player player1 = new Player(2500, "Maarten", 0);
        Player player2 = new Player(2500, "Remi", 1);
        Player player3 = new Player(2500, "Dirk", 2);

        playerService.joinRoom(room.getId(), player1.getUserId());
        playerService.joinRoom(room.getId(), player2.getUserId());
        playerService.joinRoom(room.getId(), player3.getUserId());

        Round round = roomService.startNewRoundForRoom(room.getId());

        assertEquals(30, round.getPot());
        assertEquals(2, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CALL, round.getCurrentPhase(), 20, false);
        roundService.saveAct(round.getId(), "Remi", ActType.FOLD, round.getCurrentPhase(), 0, false);

        assertEquals(4, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(6, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(8, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(10, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());
    }

    @Test
    public void playRoundWithFoldAfterRaise() throws RoomException, RoundException {
        Room room = roomService.addRoom("Test room", new GameRules());

        Player player1 = new Player(2500, "Maarten", 0);
        Player player2 = new Player(2500, "Remi", 1);
        Player player3 = new Player(2500, "Dirk", 2);

        playerService.joinRoom(room.getId(), player1.getUserId());
        playerService.joinRoom(room.getId(), player2.getUserId());
        playerService.joinRoom(room.getId(), player3.getUserId());

        Round round = roomService.startNewRoundForRoom(room.getId());

        assertEquals(2, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CALL, round.getCurrentPhase(), 20, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 10, false);

        assertEquals(4, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Remi", ActType.BET, round.getCurrentPhase(), 50, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.RAISE, round.getCurrentPhase(), 100, false);
        roundService.saveAct(round.getId(), "Maarten", ActType.FOLD, round.getCurrentPhase(), 0, false);
        assertEquals(Phase.FLOP, round.getCurrentPhase());
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 50, false);

        assertEquals(260, round.getPot());
        assertEquals(9, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(11, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(260, round.getPot());
        assertEquals(13, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());

        final Player winner = roundService.determineWinner(round.getId());
        System.out.println(winner);

        int coinSum = round.getActivePlayers().stream().mapToInt(Player::getChipCount).sum() + round.getPot();
        roundService.distributeCoins(round.getId(), winner);
        assertEquals(coinSum, round.getActivePlayers().stream().mapToInt(p -> p.getChipCount()).sum());
    }

    @Test
    public void playRoundWithBetAndMultipleRaise() throws RoomException, RoundException {
        Room room = roomService.addRoom("Test room", new GameRules());

        Player player1 = new Player(2500, "Maarten", 0);
        Player player2 = new Player(2500, "Remi", 1);
        Player player3 = new Player(2500, "Dirk", 2);

        playerService.joinRoom(room.getId(), player1.getUserId());
        playerService.joinRoom(room.getId(), player2.getUserId());
        playerService.joinRoom(room.getId(), player3.getUserId());

        Round round = roomService.startNewRoundForRoom(room.getId());

        assertEquals(2, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CALL, round.getCurrentPhase(), 20, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 10, false);

        assertEquals(4, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.BET, round.getCurrentPhase(), 240, false);
        roundService.saveAct(round.getId(), "Remi", ActType.RAISE, round.getCurrentPhase(), 390, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.RAISE, round.getCurrentPhase(), 510, false);
        roundService.saveAct(round.getId(), "Maarten", ActType.CALL, round.getCurrentPhase(), 270, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 120, false);

        assertEquals(1590, round.getPot());
        assertEquals(9, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(12, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0, false);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0, false);

        assertEquals(1590, round.getPot());
        assertEquals(15, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());

        final Player winner = roundService.determineWinner(round.getId());
        System.out.println(winner);

        int coinSum = round.getActivePlayers().stream().mapToInt(Player::getChipCount).sum() + round.getPot();
        roundService.distributeCoins(round.getId(), winner);
        assertEquals(coinSum, round.getActivePlayers().stream().mapToInt(p -> p.getChipCount()).sum());
    }
}
