package be.kdg.gameservice.round.service;

import be.kdg.gameservice.UtilTesting;
import be.kdg.gameservice.round.service.impl.HandServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HandServiceImplTest extends UtilTesting {
    @Test
    public void testImmutabilityAttributes() {
        testImmutabilityAttributes(HandServiceImpl.class);
    }
}
