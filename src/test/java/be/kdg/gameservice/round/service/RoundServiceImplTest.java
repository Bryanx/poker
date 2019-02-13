package be.kdg.gameservice.round.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.round.exception.RoundException;
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

    @Before
    public void setup() {
        provideTestDataRound(roundRepository);
    }

    @Test
    public void testStartNewRound() {
        Round round = roundService.startNewRound(new ArrayList<>(), 2);
        assertEquals(0, round.getActs().size());
        assertEquals(5, round.getCards().size());
        assertEquals(0, round.getPlayersInRound().size());
        assertEquals(Phase.PRE_FLOP, round.getCurrentPhase());
    }

    @Test(expected = RoundException.class)
    public void testSaveActFail() throws RoundException {
        roundService.saveAct(testableRoundId, testableUserId, ActType.RAISE, Phase.PRE_FLOP, 25);
        fail("Act should not be possible for this player at this time in the round.");
    }

    @Test
    public void testGetPossibleActs() throws RoundException {
        List<ActType> possibleActs = roundService.getPossibleActs(testableRoundId, testableUserId);
        assertEquals(3, possibleActs.size());
        assertTrue(possibleActs.contains(ActType.BET)
                && possibleActs.contains(ActType.CHECK)
                && possibleActs.contains(ActType.FOLD));
    }
}
