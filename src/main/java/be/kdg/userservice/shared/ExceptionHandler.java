package be.kdg.userservice.shared;

import be.kdg.userservice.notification.exception.NotificationException;
import be.kdg.userservice.user.controller.dto.ErrorDto;
import be.kdg.userservice.user.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value =
            {UserException.class, UsernameNotFoundException.class, NotificationException.class})
    public ResponseEntity<ErrorDto> handleControllerExceptions(Exception ex) {
        return new ResponseEntity<>(new ErrorDto("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
