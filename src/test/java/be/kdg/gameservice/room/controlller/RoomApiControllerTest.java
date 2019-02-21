package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.RequestType;
import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.RoomService;
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
public class RoomApiControllerTest extends UtilTesting {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomService roomService;

    @Before
    public void setup() {
        provideTestDataRooms(roomRepository);
    }

    @Test
    public void leaveRoom() throws Exception {
        // joinRoom();
        int numberOfPlayersBeforeRequest = roomService.getRoom(testableRoomIdWithPlayers).getPlayersInRoom().size();
        testMockMvc("/rooms/" + testableRoomIdWithPlayers + "/leave-room", "", mockMvc, RequestType.DELETE);
        assertEquals(numberOfPlayersBeforeRequest - 1, roomService.getRoom(testableRoomIdWithPlayers).getPlayersInRoom().size());
    }
}