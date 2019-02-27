package be.kdg.gameservice.room.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.PrivateRoom;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.PrivateRoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PrivateRoomServiceImplTest extends UtilTesting {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private PrivateRoomService privateRoomService;

    @Before
    public void setUp() throws Exception {
        this.provideTestDataPrivateRooms(roomRepository);
    }

    @Test
    public void getPrivateRooms() {
        assertEquals(2, privateRoomService.getPrivateRooms(this.testableUserId).size());
    }

    @Test
    public void getPrivateRoom() throws RoomException {
        PrivateRoom privateRoom = privateRoomService.getPrivateRoom(this.testablePrivateRoomId, this.testableUserId);
        assertEquals(0, privateRoom.getPlayersInRoom().size());
        assertEquals(0, privateRoom.getWhiteListedPlayers().size());
        assertEquals(1200, privateRoom.getGameRules().getStartingChips());
        assertEquals(12, privateRoom.getGameRules().getSmallBlind());
        assertEquals(24, privateRoom.getGameRules().getBigBlind());
        assertEquals(6, privateRoom.getGameRules().getMaxPlayerCount());
        assertEquals(15, privateRoom.getGameRules().getPlayDelay());
    }
}