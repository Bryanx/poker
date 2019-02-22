package be.kdg.mobile_client.shared;

import org.junit.Test;

import static org.junit.Assert.*;

public class UsernameValidatorTest {

    @Test
    public void isValidUsername() {
        assertTrue(UsernameValidator.isValidUsername("michael"));
        assertTrue(UsernameValidator.isValidUsername("tommy"));
        assertTrue(UsernameValidator.isValidUsername("bryan"));
        assertFalse(UsernameValidator.isValidUsername("a"));
        assertFalse(UsernameValidator.isValidUsername("system"));
        assertFalse(UsernameValidator.isValidUsername("@#$@#$"));
    }
}