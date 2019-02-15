package be.kdg.gameservice.room.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class RoomTest {
    @Test
    public void roomCreation() {
        Room room = new Room(GameRules.TEXAS_HOLD_EM, "Test room");
        assertEquals(room.getGameRules(), GameRules.TEXAS_HOLD_EM);
        assertEquals(room.getRounds().size(), 0);
        assertEquals(room.getPlayersInRoom().size(), 0);
    }
}