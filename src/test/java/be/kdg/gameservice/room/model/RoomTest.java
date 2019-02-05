package be.kdg.gameservice.room.model;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class RoomTest extends ImmutabilityTesting {
    private Room room;

    @Before
    public void setup() {
        this.room = new Room(GameRules.TEXAS_HOLD_EM, "Test room");
    }

    @Test
    public void testRoomCreation() {
        assertEquals(room.getGameRules(), GameRules.TEXAS_HOLD_EM);
        assertEquals(room.getRounds().size(), 0);
        assertEquals(room.getPlayersInRoom().size(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPlayersInRound() {
        testImmutabilityCollection(room.getPlayersInRoom());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPlayedRounds() {
        testImmutabilityCollection(room.getRounds());
    }
}