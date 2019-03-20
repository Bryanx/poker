package be.kdg.mobile_client.shared;

import org.junit.Test;

import be.kdg.mobile_client.shared.validators.EmailValidator;

import static org.junit.Assert.*;

public class EmailValidatorTest {

    @Test
    public void isValidEmail() {
        assertTrue(EmailValidator.isValidEmail("name@email.com"));
        assertTrue(EmailValidator.isValidEmail("aad@asd.asd"));
        assertTrue(EmailValidator.isValidEmail("asd@asdasd.coasdm"));
        assertFalse(EmailValidator.isValidEmail("asdas@asdasd"));
        assertFalse(EmailValidator.isValidEmail("asdasasdasd"));
        assertFalse(EmailValidator.isValidEmail(""));
    }
}