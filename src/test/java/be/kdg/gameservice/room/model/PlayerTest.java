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
        this.player = new Player(GameRules.TEXAS_HOLD_EM.getStartingChips(), "Jarne");
    }

    @Test
    public void testCreatePlayer() {
        assertEquals(player.getChipCount(), GameRules.TEXAS_HOLD_EM.getStartingChips());
        assertEquals(player.getHandType(), HandType.BAD);
        assertEquals(player.getLastAct(), ActType.UNDECIDED);
        assertEquals(player.getName(), "Jarne");
        assertTrue(player.isActive());
        assertFalse(player.isInRound());
    }

    //TODO: remove?
    /*
    @Test
    public void testResetPlayer() {
        player.setInPlay(true);
        player.setHandType(HandType.STRAIGHT_FLUSH);

        player.resetPlayer();

        assertEquals(HandType.BAD, player.getHandType());
        assertFalse(player.isInPlay());
    }
    */
}