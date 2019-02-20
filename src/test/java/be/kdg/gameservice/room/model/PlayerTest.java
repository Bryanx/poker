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
        this.player = new Player(new GameRules(8, 16, 25, 2500, 5).getStartingChips(), "1");
    }

    @Test
    public void createPlayer() {
        assertEquals(player.getChipCount(), new GameRules(8, 16, 25, 2500, 5).getStartingChips());
        assertEquals(player.getHandType(), HandType.BAD);
        assertEquals(player.getLastAct(), ActType.UNDECIDED);
        assertEquals(player.getUserId(), "1");
        assertTrue(player.isInRoom());
        assertFalse(player.isInRound());
    }

    @Test
    public void resetPlayer() {
        player.setInRound(false);
        player.setHandType(HandType.STRAIGHT_FLUSH);
        player.setLastAct(ActType.RAISE);

        player.resetPlayer();

        assertEquals(HandType.BAD, player.getHandType());
        assertEquals(ActType.UNDECIDED, player.getLastAct());
        assertTrue(player.isInRound());
    }

}