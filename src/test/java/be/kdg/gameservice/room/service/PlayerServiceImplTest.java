package be.kdg.gameservice.room.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.PlayerService;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.exception.RoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PlayerServiceImplTest extends UtilTesting  {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private RoomRepository roomRepository;

    @Before
    public void setup() {
        provideTestDataRooms(roomRepository);
    }

    @Test(expected = RoomException.class)
    public void deletePlayerFail() throws RoomException, RoundException {
        playerService.leaveRoom(testableRoomIdWithoutPlayers, "2");
        fail("The player with userId '2' should not be present in this room");
    }
}