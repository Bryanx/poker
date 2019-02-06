package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.ImmutabilityTesting;
import be.kdg.gameservice.room.controller.RoomApiController;
import be.kdg.gameservice.room.persistence.RoomRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class RoomApiControllerTest extends ImmutabilityTesting {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomRepository roomRepository;
    private int roomId;

    @Before
    public void setup() {
        roomId = roomRepository.findAll().get(0).getId();
    }

    @Test
    public void testImmutability() {
        testImmutabilityClass(RoomApiController.class);
    }

    @Test
    public void testSavePlayer() throws Exception {
        mockMvc.perform(post("/api/rooms/" + roomId  +"/players/testplayer")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void testStartNewRound() throws Exception {
        mockMvc.perform(post("/api/rooms/" + roomId + "/start-new-round")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
