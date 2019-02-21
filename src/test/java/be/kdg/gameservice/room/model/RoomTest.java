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
        Room room = new Room(new GameRules(8, 16, 25, 2500, 5), "Test room");
        assertEquals(room.getGameRules().getStartingChips(), new GameRules(8, 16, 25, 2500, 5).getStartingChips());
        assertEquals(room.getGameRules().getPlayDelay(), new GameRules(8, 16, 25, 2500, 5).getPlayDelay());
        assertEquals(room.getGameRules().getBigBlind(), new GameRules(8, 16, 25, 2500, 5).getBigBlind());
        assertEquals(room.getRounds().size(), 0);
        assertEquals(room.getPlayersInRoom().size(), 0);
    }
}