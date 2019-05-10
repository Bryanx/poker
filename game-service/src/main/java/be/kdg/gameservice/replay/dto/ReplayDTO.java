package be.kdg.gameservice.replay.dto;

import be.kdg.gameservice.replay.model.ReplayLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplayDTO {
    private int id;
    private String roomName;
    private int roundNumber;
    private List<ReplayLine> lines;
}
