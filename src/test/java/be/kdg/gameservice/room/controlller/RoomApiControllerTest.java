package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.RequestType;
import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.controller.dto.PrivateRoomDTO;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.PrivateRoomService;
import be.kdg.gameservice.room.service.api.RoomService;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
    @Autowired
    private PrivateRoomService privateRoomService;

    @Before
    public void setup() {
        provideTestDataPrivateRooms(roomRepository);
    }

    @Test
    public void leaveRoom() throws Exception {
        int numberOfPlayersBeforeRequest = roomService.getRoom(testableRoomIdWithPlayers).getPlayersInRoom().size();
        testMockMvc("/rooms/" + testableRoomIdWithPlayers + "/leave-room", "", mockMvc, RequestType.DELETE);
        assertEquals(numberOfPlayersBeforeRequest - 1, roomService.getRoom(testableRoomIdWithPlayers).getPlayersInRoom().size());
    }

    @Test
    public void addAndDeleteRoom() throws Exception {
        int numberOfRooms = roomRepository.findAll().size();

        //Test add room
        RoomDTO roomDTO = new RoomDTO(1, "jos room", new GameRules(), new ArrayList<>());
        String json = new Gson().toJson(roomDTO);
        testMockMvc("/rooms", json, mockMvc, RequestType.POST);
        assertEquals(numberOfRooms + 1, roomRepository.findAll().size());

        //Test delete room
        Room room = roomRepository.findAll().stream()
                .filter(r -> r.getName().equalsIgnoreCase("jos room")).findAny().orElseThrow(Exception::new);
        testMockMvc("/rooms/" + room.getId(), "", mockMvc, RequestType.DELETE);
        assertEquals(numberOfRooms, roomRepository.findAll().size());
    }

    @Test
    public void changeRoom() throws Exception {
        RoomDTO roomDTO = new RoomDTO(testableRoomIdWithoutPlayers, "josef", new GameRules(16, 32, 20, 5000, 4, 1, 50), new ArrayList<>());
        String json = new Gson().toJson(roomDTO);
        testMockMvc("/rooms/" + testableRoomIdWithoutPlayers, json, mockMvc, RequestType.PUT);
        assertEquals("josef", roomRepository.findById(testableRoomIdWithoutPlayers).orElseThrow(Exception::new).getName());
        assertEquals(5000, roomRepository.findById(testableRoomIdWithoutPlayers).orElseThrow(Exception::new).getGameRules().getStartingChips());
    }

    @Test
    public void addPrivateRoom() throws Exception {
        int privateRoomSize = privateRoomService.getPrivateRooms(testableUserId).size();

        PrivateRoomDTO privateRoomDTO = new PrivateRoomDTO(0, "test private room", new GameRules(), new ArrayList<>(), new ArrayList<>());
        String json = new Gson().toJson(privateRoomDTO);

        testMockMvc("/rooms/private",json, mockMvc, RequestType.POST);
        assertEquals(privateRoomSize + 1, privateRoomService.getPrivateRooms(testableUserId).size());
    }

    @Test
    public void addToWhitelist() throws Exception {
        String testUserId = "testUserId";
        int whiteListedUsersSize = privateRoomService.getPrivateRoom(testablePrivateRoomId, testableUserId).getWhiteListedUsers().size();
        testMockMvc("/rooms/private/" + this.testablePrivateRoomId + "/add-user/" + testUserId, "", mockMvc, RequestType.PATCH);
        assertEquals(whiteListedUsersSize + 1, privateRoomService.getPrivateRoom(testablePrivateRoomId, testableUserId).getWhiteListedUsers().size());
    }

    @Test
    public void removeFromWhitelist() throws Exception {
        String testUserId = this.testableUserId;
        testMockMvc("/rooms/private/" + this.testablePrivateRoomId + "/remove-user/" + testUserId, "", mockMvc, RequestType.PATCH);
        //TODO: fix this maarten!
    }
}