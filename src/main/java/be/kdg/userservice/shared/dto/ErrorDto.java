package be.kdg.userservice.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple DTO to wrap an error that is send by the exception handler.
 *
 * @see be.kdg.userservice.shared.ExceptionHandler
 */
@Data
@AllArgsConstructor
public class ErrorDto {
    private String error;
    private String error_description;
}
