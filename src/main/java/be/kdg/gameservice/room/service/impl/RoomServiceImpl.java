package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.PlayerRepository;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * This service will be used to manage the ongoing activity of a specific room.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {
    private final PlayerRepository playerRepository;
    private final RoomRepository roomRepository;
    private final RoundService roundService;


    /**
     * Creates a new room based on room object passed to roomRepository save method
     * @param room
     * @return
     */
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Returns room based on roomName
     * Carefull, roomname should be unique.
     * @param roomName
     * @return
     */
    @Override
    public Room getRoomByName(String roomName) {
        return roomRepository.getRoomByName(roomName);
    }

    /**
     * Adds a player to a room.
     *
     * @param roomId The id of the room the player needs to be added to
     * @param userId The id of the user.
     * @return The newly created player.
     * @throws RoomException Thrown if the maximum capacity for the room is reached.
     * @see Player for extra information about player constructor
     * @see Room for extra information about helper methods.
     */
    @Override
    public Player joinRoom(int roomId, String userId) throws RoomException {
        //Get room
        Room room = getRoom(roomId);

        //Determine if room is full
        if (room.getPlayersInRoom().size() > room.getGameRules().getMaxPlayerCount())
            throw new RoomException(RoomServiceImpl.class, "Maximum player capacity is reached.");

        //Add player to room
        Player player = new Player(room.getGameRules().getStartingChips(), userId, room.getFirstEmptySeat());
        player = playerRepository.save(player);
        room.addPlayer(player);
        saveRoom(room);
        return player;
    }

    /**
     * Removes the player form the room and the current round.
     *
     * @param roomId The id of the room were the players needs to be removed from
     * @param userId The id of the player we want to delete.
     * @throws RoomException Thrown if the player was not found in the room.
     */
    @Override
    public Player leaveRoom(int roomId, String userId) throws RoomException {
        //Get data
        Room room = getRoom(roomId);
        Optional<Player> playerOpt = room.getPlayersInRoom().stream()
                .filter(player -> player.getUserId().equals(userId))
                .findAny();

        //Check optional player
        if (!playerOpt.isPresent())
            throw new RoomException(RoomServiceImpl.class, "Player was not in the room.");

        //Remove player from the room
        room.removePlayer(playerOpt.get());
        saveRoom(room);
        playerRepository.delete(playerOpt.get());
        return playerOpt.get();
    }

    @Override
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player getPlayer(String userId) {
        return playerRepository.getByUserId(userId);
    }

    /**
     * @param roomId The room the new round needs to be created for.
     * @return The updated room with the new round.
     * @throws RoomException Thrown if there are less than 2 players in the round.
     */
    @Override
    public Round startNewRoundForRoom(int roomId) throws RoomException {
        //Get room
        Room room = getRoom(roomId);

        //Determine if round can be created
        List<Player> players = room.getPlayersInRoom();
        if (players.size() < 2)
            throw new RoomException(RoomServiceImpl.class, "There must be at least 2 players int room to start a round.");
        int button = room.getRounds().size() == 0 ? 0 : room.getCurrentRound().getButton();

        //Create new round
        Round round = roundService.startNewRound(room.getPlayersInRoom(), button);
        if (room.getRounds().size() > 0) room.getCurrentRound().setFinished(true);
        room.addRound(round);
        saveRoom(room);
        return round;
    }

    /**
     * The rooms returned by this method are cached locally.
     * @return An unmodifiable collection of all the rooms from the database.
     */
    @Override
    public List<Room> getRooms() {
        return Collections.unmodifiableList(roomRepository.findAll());
    }

    /**
     * Gets the current round of a specific room, if the round does not exist,
     * than a new round will be created.
     *
     * @return The current round of the room.
     */
    @Override
    public Round getCurrentRound(int roomId) throws RoomException {
        //Get data
        Room room = getRoom(roomId);

        //Determine which round to give back
        if (room.getRounds().size() <= 0) return startNewRoundForRoom(roomId);
        else return room.getCurrentRound();
    }

    /**
     * The room returned by this method is cached locally.
     *
     * @param roomId The id of the room.
     * @return The corresponding room.
     * @throws RoomException Thrown if the room does not exists in the database.
     */
    @Override
    public Room getRoom(int roomId) throws RoomException {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomException(RoomServiceImpl.class, "The room was not found in the database."));
    }

    /**
     * @param room The room that needs to be updated or saved.
     */
    private Room saveRoom(Room room) {
        return roomRepository.save(room);
    }
}