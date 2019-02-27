package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.PrivateRoom;
import be.kdg.gameservice.room.model.WhiteListedUser;
import be.kdg.gameservice.room.persistence.PlayerRepository;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.persistence.WhiteListedPlayerRepository;
import be.kdg.gameservice.room.service.api.PrivateRoomService;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional
@Service
public class PrivateRoomServiceImpl implements PrivateRoomService{
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;
    private final WhiteListedPlayerRepository whiteListedPlayerRepository;
    private final RoundService roundService;
    private final RoomService roomService;

    @Override
    public PrivateRoom getPrivateRoom(int roomId, String userId) throws RoomException {
        //Get data
        PrivateRoom room = (PrivateRoom) roomService.getRoom(roomId);

        //Do check
        room.getWhiteListedPlayers().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny()
                .orElseThrow(() -> new RoomException(RoomServiceImpl.class, "user with userId " + userId + " was not found on the whitelist"));

        return room;
    }

    @Override
    public PrivateRoom addPrivateRoom(String userId, String name) {
        //Make room
        PrivateRoom room = new PrivateRoom(name, userId);

        //update database
        roomRepository.save(room);
        return room;
    }

    @Override
    public void addUserToWhiteList(int roomId, String userId) throws RoomException {
        //Get data
        PrivateRoom room = (PrivateRoom) roomService.getRoom(roomId);

        //Do check
        Optional<WhiteListedUser> whiteListedPlayerOpt = room.getWhiteListedPlayers().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny();

        if (whiteListedPlayerOpt.isPresent())
            throw new RoomException(RoomServiceImpl.class, "User with id " + userId + " was already whitelisted");
        if (room.getWhiteListedPlayers().size() + 1 > 6)
            throw new RoomException(RoomServiceImpl.class, "The private room with id " + room.getId() + " is full!");

        //update database
        room.addWhiteListedPlayer(new WhiteListedUser(userId));
        roomRepository.save(room);
    }

    @Override
    public void removeUserFromWhiteList(int roomId, String userId) throws RoomException {
        //Get data
        PrivateRoom room = (PrivateRoom) roomService.getRoom(roomId);
        Optional<WhiteListedUser> userOpt = room.getWhiteListedPlayers().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findAny();

        //Update database
        userOpt.ifPresent(whiteListedPlayerRepository::delete);
    }

    /**
     * Gives back all the private rooms that the user is owner or whitelisted for.
     */
    @Override
    public List<PrivateRoom> getPrivateRooms(String userId) {
        return roomRepository.findAll().stream()
                .filter(PrivateRoom.class::isInstance)
                .map(PrivateRoom.class::cast)
                .filter(room -> room.getWhiteListedPlayers().stream()
                        .anyMatch(p -> p.getUserId().equals(userId)))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }
}
