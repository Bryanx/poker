package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.round.service.impl.RoundServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class RoundApiControllerTest extends UtilTesting {
    @Test
    public void testImmutabilityAttributes() {
        testImmutabilityAttributes(RoundServiceImpl.class);
    }

    /*
    Tests are maybe useful in the future, for now they are badly broken,
    because they both require a user id.

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoundRepository roundRepository;

    private int roundId;
    private int playerId;

    @Before
    public void setup() {
        Optional<Round> round = roundRepository.findAll().stream()
                .filter(r -> !r.isFinished())
                .findAny();

        if (!round.isPresent()) fail("Nothing testable present in database.");

        roundId = round.get().getId();
        playerId = round.get().getPlayersInRound().get(0).getId();
    }


    @Test
    public void testGetPossibleActs() throws Exception {
        testMockMvc("/rounds/" + roundId + "/possible-acts", "",
                mockMvc, RequestType.GET);
    }

    @Test
    public void testSaveAct() throws Exception {
        ActDTO actDTO = new ActDTO(roundId, playerId, ActType.BET, Phase.PRE_FLOP, 10);
        String json = new Gson().toJson(actDTO);
        testMockMvc("/rounds/" + actDTO.getRoundId() + "/acts",
                json, mockMvc, RequestType.POST);
    }
    */
}
