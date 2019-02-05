package be.kdg.gameservice.room.service.api;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;

public interface RoomService {
    Player savePlayer(int roomId, String name) throws RoomException;
}
