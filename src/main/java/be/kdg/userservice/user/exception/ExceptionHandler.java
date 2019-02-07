package be.kdg.userservice.user.exception;

import be.kdg.userservice.user.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserException.class})
    public ResponseEntity<ErrorDto> handleControllerExceptions(Exception ex) {
        return new ResponseEntity<>(new ErrorDto("error", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
