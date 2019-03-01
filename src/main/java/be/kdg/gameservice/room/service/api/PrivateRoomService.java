package be.kdg.gameservice.room.service.api;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.PrivateRoom;

import java.util.List;

public interface PrivateRoomService {
    PrivateRoom getPrivateRoom(int roomId, String userId) throws RoomException;

    List<PrivateRoom> getPrivateRooms(String userId);

    List<PrivateRoom> getPrivateRoomsFromOwner(String userId);

    PrivateRoom addPrivateRoom(String userId, GameRules gameRules, String name);

    PrivateRoom addUserToWhiteList(int roomId, String userId) throws RoomException;

    PrivateRoom removeUserFromWhiteList(int roomId, String userId) throws RoomException;
}
