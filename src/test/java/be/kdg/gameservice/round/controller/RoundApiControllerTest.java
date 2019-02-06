package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.ImmutabilityTesting;
import be.kdg.gameservice.room.model.Player;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class RoundApiControllerTest extends ImmutabilityTesting {
    @Autowired
    private MockMvc mockMvc;
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
    public void testImmutability() {
        testImmutabilityClass(RoundApiController.class);
    }

    @Test
    public void testGetPossibleActs() throws Exception {
        mockMvc.perform(get("/api/rounds/" + roundId + "/players/" + playerId + "/possible-acts")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveAct() throws Exception {
        ActDTO actDTO = new ActDTO(roundId, playerId, ActType.BET, Phase.PRE_FLOP, 10);
        String json = new Gson().toJson(actDTO);

        mockMvc.perform(post("/api/rounds/acts")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
