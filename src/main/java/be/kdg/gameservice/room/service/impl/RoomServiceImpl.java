package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.service.api.RoomService;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {
    @Override
    public Player savePlayer(int roomId, String name) {
        return null;
    }
}