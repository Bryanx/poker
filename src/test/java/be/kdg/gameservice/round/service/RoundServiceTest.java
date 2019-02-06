package be.kdg.gameservice.round.service;

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

import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoundServiceTest {
    @Autowired
    private RoundService roundService;
    @Autowired
    private RoundRepository roundRepository;
    private int roundId;
    private int playerId;

    @Before
    public void setup() {
        Round round = roundRepository.findAll().get(0);
        roundId = round.getId();
        playerId = round.getPlayersInRound().get(0).getId();
    }

    @Test
    public void testGetPossibleActs() throws RoundException {
        List<ActType> possibleActs = roundService.getPossibleActs(roundId, playerId);
        assertEquals(3, possibleActs.size());
        assertTrue(possibleActs.contains(ActType.BET)
                && possibleActs.contains(ActType.CHECK)
                && possibleActs.contains(ActType.FOLD));
    }

    @Test(expected = RoundException.class)
    public void testSaveAct() throws RoundException {
        roundService.saveAct(roundId, playerId, ActType.RAISE, Phase.PRE_FLOP, 25);
        fail("Act should not be possible for this player at this time in the round.");
    }
}
