package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.UtilTesting;
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
public class RoomApiControllerTest extends UtilTesting {
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
        testMockMvcGet("/rooms", mockMvc);
    }

    @Test
    public void testGetRoom() throws Exception {
        testMockMvcGet("/rooms/" + roomId, mockMvc);
    }
















    
    //TODO: replace static player id.
    @Test
    public void testJoinRoom() throws Exception {
        testMockMvcPost("/rooms/" + roomId + "/players/20/join-room", null, mockMvc);
    }

    @Test
    public void testStartNewRound() throws Exception {
        testMockMvcPost("/rooms/" + roomId + "/rounds/start-new-round", null, mockMvc);
    }

    @Test
    public void testGetCurrentRound() throws Exception {
        testMockMvcGet("/rooms/" + roomId + "/rounds/current-round", mockMvc);
    }

    @Test
    public void testLeaveRoom() throws Exception {
       //TODO: write integration test.
    }
}
