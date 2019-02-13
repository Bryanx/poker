package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.RequestType;
import be.kdg.gameservice.UtilTesting;
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
    public void joinRoom() throws Exception {
        testMockMvc("/rooms/" + testableRoomId + "/join-room", "", mockMvc, RequestType.POST);
        assertEquals(1, roomService.getRoom(testableRoomId).getPlayersInRoom().size());
    }

    @Test
    public void testLeaveRoom() throws Exception {
        joinRoom();
        testMockMvc("/rooms/" + testableRoomId + "/leave-room", "", mockMvc, RequestType.POST);
        assertEquals(0, roomService.getRoom(testableRoomId).getPlayersInRoom().size());
    }

    @Test
    public void testStartNewRound() throws Exception {
        testMockMvc("/rooms/" + testableRoomId + "/start-new-round", "", mockMvc, RequestType.POST);
        assertEquals(1, roomService.getRoom(testableRoomId).getRounds().size());
    }

    @Test
    public void testGetCurrentRound() throws Exception {
        testMockMvc("/rooms/" + testableRoomId + "/current-round", "", mockMvc, RequestType.GET);
    }
}
