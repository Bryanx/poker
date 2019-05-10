package be.kdg.gameservice.shared;


import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.shared.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {RoomException.class, RoundException.class})
    public ResponseEntity<ErrorDTO> handleControllerExceptions(Exception ex) {
        LOGGER.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorDTO(ex.getCause().toString(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
