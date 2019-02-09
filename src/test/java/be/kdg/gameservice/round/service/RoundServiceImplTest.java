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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoundServiceImplTest extends UtilTesting {
    @Autowired
    private RoundService roundService;
    @Autowired
    private RoundRepository roundRepository;
    private int roundId;
    private String userId;

    @Before
    public void setup() {
        Optional<Round> round = roundRepository.findAll().stream()
                .filter(r -> !r.isFinished())
                .findAny();

        if (!round.isPresent()) fail("Nothing testable present in database.");

        roundId = round.get().getId();
        userId = round.get().getPlayersInRound().get(0).getUserId();
    }

    @Test
    public void testImmutabilityAttributes() {
        testImmutabilityAttributes(RoundServiceImpl.class);
    }

    @Test
    public void testGetPossibleActs() throws RoundException {
        List<ActType> possibleActs = roundService.getPossibleActs(roundId, userId);
        assertEquals(3, possibleActs.size());
        assertTrue(possibleActs.contains(ActType.BET)
                && possibleActs.contains(ActType.CHECK)
                && possibleActs.contains(ActType.FOLD));
    }

    @Test
    public void testSaveAct() throws RoundException {
        roundService.saveAct(roundId, userId, ActType.FOLD, Phase.PRE_FLOP, 25);
    }

    @Test(expected = RoundException.class)
    public void testSaveActFail() throws RoundException {
        roundService.saveAct(roundId, userId, ActType.RAISE, Phase.PRE_FLOP, 25);
        fail("Act should not be possible for this player at this time in the round.");
    }
}
