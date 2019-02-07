package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.ImmutabilityTesting;
import be.kdg.gameservice.room.controller.RoomApiController;
import be.kdg.gameservice.room.model.Room;
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

import java.util.Optional;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        Optional<Room> roomOpt = roomRepository.findAll().stream()
                .filter(room -> room.getPlayersInRoom().size() >= 2)
                .findAny();

        if (!roomOpt.isPresent()) fail("Nothing testable present in database.");
        roomId = roomOpt.get().getId();
    }

    @Test
    public void testImmutability() {
        testImmutabilityClass(RoomApiController.class);
    }

    @Test
    public void testGetRooms() throws Exception {
        mockMvc.perform(get("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetRoom() throws Exception {
        mockMvc.perform(get("/api/rooms/" + roomId)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void testSavePlayer() throws Exception {
        mockMvc.perform(post("/api/rooms/" + roomId + "/players/20")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void testStartNewRound() throws Exception {
        mockMvc.perform(post("/api/rooms/" + roomId + "/start-new-round")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetCurrentRound() throws Exception {
        mockMvc.perform(get("/api/rooms/" + roomId + "/rounds/current-round")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
