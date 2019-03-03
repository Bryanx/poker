package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.PrivateRoom;
import be.kdg.gameservice.room.model.WhiteListedUser;
import be.kdg.gameservice.room.persistence.WhiteListedPlayerRepository;
import be.kdg.gameservice.room.service.api.PrivateRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * This class is used for the management of private rooms
 */
@RequiredArgsConstructor
@Transactional
@Service
public class PrivateRoomServiceImpl implements PrivateRoomService{
    private final WhiteListedPlayerRepository whiteListedPlayerRepository;
    private final RoomServiceImpl roomService;

    /**
     * Gets a specific private room out of the database. Before that the room is retrieved,
     * the user id will be checked on if it is on the white list.
     *
     * @param roomId The id of the room that needs to be returned.
     * @param userId The user id that needs to be checked on.
     * @return The private room.
     * @throws RoomException Thrown if the user wasn't on the white list.
     */
    @Override
    public PrivateRoom getPrivateRoom(int roomId, String userId) throws RoomException {
        //Get data
        PrivateRoom room = (PrivateRoom) roomService.getRoom(roomId);

        //Do check
        room.getWhiteListedUsers().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new RoomException(PrivateRoomServiceImpl.class, "user with userId " + userId + " was not found on the whitelist"));

        return room;
    }

    /**
     * Adds a private room to the database.
     *
     * @param userId The owner of the private room.
     * @param name The name of the room
     * @return The newly created private room.
     */
    @Override
    public PrivateRoom addPrivateRoom(String userId, GameRules gameRules, String name) {
        //Make room
        PrivateRoom room = new PrivateRoom(gameRules, name, userId);

        //update database
        roomService.saveRoom(room);
        return room;
    }

    /**
     * Adds a user to the whitelist.
     *
     * @param roomId The id of the room.
     * @param userId The user id that needs be added to the whitelist.
     * @throws RoomException Thrown if the whitelist is full or if
     */
    @Override
    public PrivateRoom addUserToWhiteList(int roomId, String userId) throws RoomException {
        //Get data
        PrivateRoom room = (PrivateRoom) roomService.getRoom(roomId);

        //Do check
        Optional<WhiteListedUser> whiteListedPlayerOpt = room.getWhiteListedUsers().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny();

        if (whiteListedPlayerOpt.isPresent())
            throw new RoomException(PrivateRoomServiceImpl.class, "User with id " + userId + " was already whitelisted");

        //update database
        room.addWhiteListedPlayer(new WhiteListedUser(userId));
        return (PrivateRoom) roomService.saveRoom(room);
    }

    /**
     * Removes a user from the whitelist.
     *
     * @param roomId The id of the room
     * @param userId The user that needs to be removed from the whitelist.
     * @throws RoomException Thrown if the room was not found in the database.
     */
    @Override
    public PrivateRoom removeUserFromWhiteList(int roomId, String userId) throws RoomException {
        //Get data
        PrivateRoom room = (PrivateRoom) roomService.getRoom(roomId);
        Optional<WhiteListedUser> userOpt = room.getWhiteListedUsers().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findAny();

        //Update database
        userOpt.ifPresent(entity -> {
            whiteListedPlayerRepository.delete(entity);
            room.deleteWhiteListedPlayer(userOpt.get());
        });
        return room;
    }

    /**
     * Gets all the private rooms that are from the specified user and
     * that the user is whitelisted on.
     *
     * @param userId The id of the user.
     * @return All the authenticated private rooms.
     */
    @Override
    public List<PrivateRoom> getPrivateRooms(String userId) {
        return roomService.getRooms(PrivateRoom.class).stream()
                .map(PrivateRoom.class::cast)
                .filter(room -> room.getWhiteListedUsers().stream()
                        .anyMatch(p -> p.getUserId().equals(userId)))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Gets all the private rooms form the database that the given user is the
     * owner of.
     *
     * @param userId The id of the owner.
     * @return A list of private rooms that
     */
    @Override
    public List<PrivateRoom> getPrivateRoomsFromOwner(String userId) {
       return roomService.getRooms(PrivateRoom.class).stream()
               .map(PrivateRoom.class::cast)
               .filter(room -> room.getOwnerId().equals(userId))
               .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }
}
