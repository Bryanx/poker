package be.kdg.gameservice.room.model;

import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.HandType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public final class PlayerTest {
    private Player player;

    @Before
    public void setup() {
        this.player = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), 1);
    }

    @Test
    public void testCreatePlayer() {
        assertEquals(player.getChipCount(), GameRules.TEXAS_HOLD_EM.getStartingChips());
        assertEquals(player.getHandType(), HandType.BAD);
        assertEquals(player.getLastAct(), ActType.UNDECIDED);
        assertEquals(player.getUserId(), 1);
        assertTrue(player.isActive());
        assertFalse(player.isInRound());
    }

    @Test
    public void testResetPlayer() {
        player.setInRound(false);
        player.setHandType(HandType.STRAIGHT_FLUSH);
        player.setLastAct(ActType.RAISE);

        player.resetPlayer();

        assertEquals(HandType.BAD, player.getHandType());
        assertEquals(ActType.UNDECIDED, player.getLastAct());
        assertTrue(player.isInRound());
    }

}