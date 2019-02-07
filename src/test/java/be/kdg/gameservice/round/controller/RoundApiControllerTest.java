package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.persistence.RoundRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class RoundApiControllerTest extends UtilTesting {
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
    public void testImmutability() {
        testImmutabilityClass(RoundApiController.class);
    }

    @Test
    public void testGetPossibleActs() throws Exception {
        testMockMvcGet("/rounds/" + roundId + "/players/" + playerId + "/possible-acts", mockMvc);
    }

    @Test
    public void testSaveAct() throws Exception {
        ActDTO actDTO = new ActDTO(roundId, playerId, ActType.BET, Phase.PRE_FLOP, 10);
        String json = new Gson().toJson(actDTO);
        testMockMvcPost("/rounds/" + actDTO.getRoundId() + "/players/" + actDTO.getPlayerId() + "/acts",
                json, mockMvc);
    }
}
