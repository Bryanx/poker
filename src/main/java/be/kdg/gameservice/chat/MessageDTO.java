package be.kdg.gameservice.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
final class MessageDTO {
    private String name;
    private String content;
}
