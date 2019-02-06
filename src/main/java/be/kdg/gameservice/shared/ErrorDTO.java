package be.kdg.gameservice.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ErrorDTO {
    private String error;
    private String error_description;
}
