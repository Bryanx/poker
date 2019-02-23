package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.RequestType;
import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.persistence.RoundRepository;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class RoundApiControllerTest extends UtilTesting {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoundRepository roundRepository;

    @Before
    public void setup() {
       provideTestDataRound(roundRepository);
    }

//    @Test
//    public void saveAct() throws Exception {
//        ActDTO actDTO = new ActDTO(testableUserId, testableRoundIdWithPlayers, ActType.BET, Phase.PRE_FLOP, 10);
//        String json = new Gson().toJson(actDTO);
//        testMockMvc("/rounds/" + actDTO.getRoundId(),
//                json, mockMvc, RequestType.POST);
//
//        int numberOfActs = roundRepository.findById(testableRoundIdWithPlayers)
//                .orElseThrow(Exception::new)
//                .getActs().size();
//        assertEquals(1, numberOfActs);
//    }
}
