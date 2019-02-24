package be.kdg.gameservice.room.controlller;

import be.kdg.gameservice.RequestType;
import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
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
import java.util.List;

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
        int numberOfPlayersBeforeRequest = roomService.getRoom(testableRoomIdWithPlayers).getPlayersInRoom().size();
        testMockMvc("/rooms/" + testableRoomIdWithPlayers + "/leave-room", "", mockMvc, RequestType.DELETE);
        assertEquals(numberOfPlayersBeforeRequest - 1, roomService.getRoom(testableRoomIdWithPlayers).getPlayersInRoom().size());
    }

    @Test
    public void addAndDeleteRoom() throws Exception {
        int numberOfRooms = roomRepository.findAll().size();

        //Test add room
        RoomDTO roomDTO = new RoomDTO(1, "jos room", new GameRules(8, 16, 25, 2500, 5), new ArrayList<>());
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
        RoomDTO roomDTO = new RoomDTO(testableRoomIdWithoutPlayers, "josef", new GameRules(16, 32, 20, 5000, 4), new ArrayList<>());
        String json = new Gson().toJson(roomDTO);
        testMockMvc("/rooms/" + testableRoomIdWithoutPlayers, json, mockMvc, RequestType.PUT);
        assertEquals("josef", roomRepository.findById(testableRoomIdWithoutPlayers).orElseThrow(Exception::new).getName());
        assertEquals(new GameRules(16, 32, 20, 5000, 4).getStartingChips(), roomRepository.findById(testableRoomIdWithoutPlayers).orElseThrow(Exception::new).getGameRules().getStartingChips());
    }

    @Test
    public void startNewRound() throws Exception {
        testMockMvc("/rooms/" + testableRoomIdWithPlayers + "/start-new-round", "", mockMvc, RequestType.POST);
        assertEquals(1, roomService.getRoom(testableRoomIdWithPlayers).getRounds().size());
    }

    @Test
    public void getCurrentRound() throws Exception {
        testMockMvc("/rooms/" + testableRoomIdWithPlayers + "/current-round", "", mockMvc, RequestType.GET);
    }
}