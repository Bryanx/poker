package be.kdg.gameservice.room.service.api;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;

public interface RoomService {
    Player savePlayer(int roomId, int userId) throws RoomException;

    Room startNewRoundForRoom(int roomId) throws RoomException;
}
