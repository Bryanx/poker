package be.kdg.gameservice.room.service.api;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.round.model.Round;

import java.util.List;

public interface RoomService {
    Player savePlayer(int roomId, int userId) throws RoomException;

    Player deletePlayer(int roomId, int playerId) throws RoomException;

    Round startNewRoundForRoom(int roomId) throws RoomException;

    List<Room> getRooms();

    Round getCurrentRound(int roomId) throws RoomException;

    Room getRoom(int roomId) throws RoomException;
}
