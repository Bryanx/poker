package be.kdg.gameservice.room.service.api;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.round.exception.RoundException;

public interface PlayerService {
    Player joinRoom(int roomId, String userId) throws RoomException;

    Player leaveRoom(int roomId, String userId) throws RoomException, RoundException;

    Player savePlayer(Player player);

    Player getPlayer(String userId) throws RoomException;
}
