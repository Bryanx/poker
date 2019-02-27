package be.kdg.gameservice.room.service.api;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.PrivateRoom;

import java.util.List;

public interface PrivateRoomService {
    PrivateRoom getPrivateRoom(int roomId, String userId) throws RoomException;

    List<PrivateRoom> getPrivateRooms(String userId);

    List<PrivateRoom> getPrivateRoomsFromOwner(String userId);

    PrivateRoom addPrivateRoom(String userId, String name);

    void addUserToWhiteList(int roomId, String userId) throws RoomException;

    void removeUserFromWhiteList(int roomId, String userId) throws RoomException;
}
