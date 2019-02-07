package be.kdg.gameservice.chat;

import be.kdg.gameservice.ImmutabilityTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChatControllerTest extends ImmutabilityTesting {
    @Test
    public void testImmutability() {
        testImmutabilityClass(ChatController.class);
    }
}
