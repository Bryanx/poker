package be.kdg.gameservice.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MessageDTO {
    private String name;
    private String content;
}
