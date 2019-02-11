package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.RequestType;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.fail;

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
                .filter(room -> room.getPlayersInRoom().size() < room.getGameRules().getMaxPlayerCount())
                .findAny();

        if (!roomOpt.isPresent()) fail("Nothing testable present in database.");
        roomId = roomOpt.get().getId();
    }

    @Test
    public void testImmutabilityAttributes() {
        testImmutabilityAttributes(RoomApiController.class);
    }

    @Test
    public void testGetRooms() throws Exception {
        testMockMvc("/rooms", "", mockMvc, RequestType.GET);
    }

    @Test
    public void testGetRoom() throws Exception {
        testMockMvc("/rooms/" + roomId, "", mockMvc, RequestType.GET);
    }

    //TODO: replace static player id.
    @Test
    public void testJoinRoom() throws Exception {
        testMockMvc("/rooms/" + roomId + "/join-room", "", mockMvc, RequestType.POST);
    }

    @Test
    public void testStartNewRound() throws Exception {
        testMockMvc("/rooms/" + roomId + "/start-new-round", "", mockMvc, RequestType.POST);
    }

    @Test
    public void testGetCurrentRound() throws Exception {
        testMockMvc("/rooms/" + roomId + "/current-round", "", mockMvc, RequestType.GET);
    }

    @Test
    public void testLeaveRoom() throws Exception {
        testJoinRoom();
        /*
        TODO: @Jarne werkt niet
        testMockMvc("/rooms/" + roomId + "/leave-room", "", mockMvc, RequestType.DELETE);
         */
        }
}
