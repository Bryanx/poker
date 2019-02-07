package be.kdg.gameservice.chat;

import be.kdg.gameservice.UtilTesting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ChatControllerTest extends UtilTesting {
    @Test
    public void testImmutability() {
        testImmutabilityClass(ChatController.class);
    }
}
