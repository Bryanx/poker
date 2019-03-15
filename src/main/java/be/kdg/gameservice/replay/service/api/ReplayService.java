package be.kdg.gameservice.replay.service.api;

import be.kdg.gameservice.replay.model.Replay;

import java.util.List;

public interface ReplayService {
    List<Replay> getReplays(String ownerId);
}
