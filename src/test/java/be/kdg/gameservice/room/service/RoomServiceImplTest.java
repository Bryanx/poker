package be.kdg.gameservice.room.service;

import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.RoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class RoomServiceImplTest {
    @Autowired
    private RoomService roomService;
    @Autowired
    private RoomRepository roomRepository;

    @Before
    public void setup() {
        roomRepository.deleteAll();

        Room room1 = new Room(GameRules.TEXAS_HOLD_EM, "test room 1");
        Room room2 = new Room(GameRules.TEXAS_HOLD_EM, "test room 2");
        Room room3 = new Room(GameRules.TEXAS_HOLD_EM_DIFFICULT, "test room 3");

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);
    }

    @Test
    public void getRooms() {
        assertEquals(3, roomService.getRooms().size());
    }
}
