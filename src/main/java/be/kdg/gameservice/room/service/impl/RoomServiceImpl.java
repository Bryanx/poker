package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * This service will be used to manage the ongoing activity of a specific room.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@Transactional
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Adds a player to a room.
     *
     * @param roomId The id of the room the player needs to be added to
     * @param name The name of the player
     * @return The newly created player.
     * @throws RoomException Thrown if the maximum capacity for the room is reached.
     * @see Player for extra information about player constructor
     * @see Room for extra information about helper methods.
     */
    @Override
    public Player savePlayer(int roomId, String name) throws RoomException {
        //Get data
        Room room = getRoom(roomId);

        //Determine if room is full
        if (room.getPlayersInRoom().size() > room.getGameRules().getMaxPlayerCount())
            throw new RoomException(RoomServiceImpl.class, "Max player count is reached.");

        //Add player to room
        Player player = new Player(room.getGameRules().getStartingChips(), name);
        room.addPlayer(player);
        saveRoom(room);
        return player;
    }

    /**
     * @param roomId The id of the room.
     * @return The corresponding room.
     * @throws RoomException Thrown if the room does not exists in the database.
     */
    private Room getRoom(int roomId) throws RoomException {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(RoomServiceImpl.class, "The room was not found in the database."));
    }

    /**
     * @param room The room that needs to be updated or saved.
     */
    private void saveRoom(Room room) {
        roomRepository.save(room);
    }
}