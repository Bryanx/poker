package be.kdg.userservice.shared;

import be.kdg.userservice.notification.exception.NotificationException;
import be.kdg.userservice.shared.dto.ErrorDto;
import be.kdg.userservice.user.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This handler will capture all the exceptions that are thrown from the controllers. There are 2
 * things that will happen:
 * - The exception will be logged with a warning.
 * - The exception will be wrapped in a error DTO.
 *
 * @see ErrorDto
 */
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value =
            {UserException.class, UsernameNotFoundException.class, NotificationException.class})
    public ResponseEntity<ErrorDto> handleControllerExceptions(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorDto("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
