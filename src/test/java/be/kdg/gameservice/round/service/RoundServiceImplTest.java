package be.kdg.gameservice.round.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Act;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.persistence.RoundRepository;
import be.kdg.gameservice.round.service.api.RoundService;
import be.kdg.gameservice.round.service.impl.RoundServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RoundServiceImplTest extends UtilTesting {
    @Autowired
    private RoundService roundService;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private RoomService roomService;

    @Before
    public void setup() {
        provideTestDataRound(roundRepository);
    }

    @Test
    public void getRounds() {
        List<Round> rounds = roundService.getRounds();
        System.out.println(rounds.size());
        System.out.println(rounds);
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
        roundService.saveAct(testableRoundIdWithPlayers, testableUserId, ActType.RAISE, Phase.PRE_FLOP, 25);
        fail("Act should not be possible for this player at this time in the round.");
    }

    @Test
    public void getPossibleActs() throws RoundException {
        List<ActType> possibleActs = roundService.getPossibleActs(testableRoundIdWithPlayers, testableUserId);
        assertEquals(3, possibleActs.size());
        assertTrue(possibleActs.contains(ActType.BET)
                && possibleActs.contains(ActType.CHECK)
                && possibleActs.contains(ActType.FOLD));
    }

    @Test
    public void playRoundWithCheck() throws RoomException, RoundException {
        Room roomMade = new Room(GameRules.TEXAS_HOLD_EM, "Test room");
        roomService.addRoom(roomMade);

        Player player1 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Maarten");
        Player player2 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Remi");
        Player player3 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Dirk");

        Room room = roomService.getRoomByName("Test room");

        roomService.joinRoom(room.getId(),player1.getUserId() );
        roomService.joinRoom(room.getId(),player2.getUserId());
        roomService.joinRoom(room.getId(),player3.getUserId() );

        roomService.startNewRoundForRoom(room.getId());

        Round round = roomService.getRoomByName("Test room").getCurrentRound();

        assertEquals(0, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(3, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());

        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(6, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(9, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(0, round.getPot());
        assertEquals(12, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());
    }

    @Test
    public void playRoundWithFold() throws RoomException, RoundException {
        Room roomMade = new Room(GameRules.TEXAS_HOLD_EM, "Test room");
        roomService.addRoom(roomMade);

        Player player1 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Maarten");
        Player player2 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Remi");
        Player player3 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Dirk");

        Room room = roomService.getRoomByName("Test room");

        roomService.joinRoom(room.getId(),player1.getUserId() );
        roomService.joinRoom(room.getId(),player2.getUserId());
        roomService.joinRoom(room.getId(),player3.getUserId() );

        roomService.startNewRoundForRoom(room.getId());

        Round round = roomService.getRoomByName("Test room").getCurrentRound();

        assertEquals(0, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.FOLD, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(3, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(5, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(7, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(9, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());
    }

    @Test
    public void playRoundWithBet() throws RoomException, RoundException {
        Room roomMade = new Room(GameRules.TEXAS_HOLD_EM, "Test room");
        roomService.addRoom(roomMade);

        Player player1 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Maarten");
        Player player2 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Remi");
        Player player3 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Dirk");

        Room room = roomService.getRoomByName("Test room");

        roomService.joinRoom(room.getId(),player1.getUserId() );
        roomService.joinRoom(room.getId(),player2.getUserId());
        roomService.joinRoom(room.getId(),player3.getUserId() );

        roomService.startNewRoundForRoom(room.getId());

        Round round = roomService.getRoomByName("Test room").getCurrentRound();

        assertEquals(0, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.BET, round.getCurrentPhase(), 50);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 50);
        roundService.saveAct(round.getId(), "Dirk", ActType.CALL, round.getCurrentPhase(), 50);

        assertEquals(3, round.getActs().size());
        assertEquals(150, round.getPot());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(6, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(9, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(150, round.getPot());
        assertEquals(12, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());
    }

    @Test
    public void playRoundWithDetermineWinner() throws RoomException, RoundException {
        Room roomMade = new Room(GameRules.TEXAS_HOLD_EM, "Test room");
        roomService.addRoom(roomMade);

        Player player1 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Maarten");
        Player player2 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Remi");
        Player player3 = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Dirk");

        Room room = roomService.getRoomByName("Test room");

        roomService.joinRoom(room.getId(),player1.getUserId() );
        roomService.joinRoom(room.getId(),player2.getUserId());
        roomService.joinRoom(room.getId(),player3.getUserId() );

        roomService.startNewRoundForRoom(room.getId());

        Round round = roomService.getRoomByName("Test room").getCurrentRound();

        assertEquals(0, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.BET, round.getCurrentPhase(), 50);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 50);
        roundService.saveAct(round.getId(), "Dirk", ActType.CALL, round.getCurrentPhase(), 50);

        assertEquals(150, round.getPot());
        assertEquals(3, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(3, round.getActivePlayers().size());
        assertEquals(Phase.FLOP, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Maarten", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.BET, round.getCurrentPhase(), 50);
        roundService.saveAct(round.getId(), "Dirk", ActType.RAISE, round.getCurrentPhase(), 100);
        roundService.saveAct(round.getId(), "Maarten", ActType.FOLD, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Remi", ActType.CALL, round.getCurrentPhase(), 50);

        assertEquals(350, round.getPot());
        assertEquals(8, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.TURN, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(10, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.RIVER, round.getCurrentPhase());

        roundService.saveAct(round.getId(), "Remi", ActType.CHECK, round.getCurrentPhase(), 0);
        roundService.saveAct(round.getId(), "Dirk", ActType.CHECK, round.getCurrentPhase(), 0);

        assertEquals(350, round.getPot());
        assertEquals(12, round.getActs().size());
        assertEquals(3, round.getPlayersInRound().size());
        assertEquals(2, round.getActivePlayers().size());
        assertEquals(Phase.SHOWDOWN, round.getCurrentPhase());

        roundService.determineWinner(round.getId());

    }
}
