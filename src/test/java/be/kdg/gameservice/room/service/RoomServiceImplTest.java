package be.kdg.gameservice.room.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.room.service.impl.RoomServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RoomServiceImplTest extends UtilTesting {
    @Test
    public void testImmutabilityAttributes() {
        testImmutabilityAttributes(RoomServiceImpl.class);
    }
}
