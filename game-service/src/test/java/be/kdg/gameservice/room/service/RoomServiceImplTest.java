package be.kdg.gameservice.room.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.PlayerService;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.exception.RoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RoomServiceImplTest extends UtilTesting {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;

    @Before
    public void setup() {
        provideTestDataRooms(roomRepository);
    }

    @Test
    public void getRooms() {
        assertEquals(3, roomService.getRooms(Room.class).size());
    }

    @Test(expected = RoomException.class)
    public void startNewRoundFail() throws RoomException, RoundException {
        roomService.getCurrentRound(testableRoomIdWithoutPlayers);
        fail("A round should not be started with less than 2 players inside one room.");
    }
}
