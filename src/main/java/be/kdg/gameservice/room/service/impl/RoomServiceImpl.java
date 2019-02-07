package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * This service will be used to manage the ongoing activity of a specific room.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@Transactional
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoundService roundService;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoundService roundService) {
        this.roomRepository = roomRepository;
        this.roundService = roundService;
        /*
        try {
            saveRoom(new Room(GameRules.TEXAS_HOLD_EM, "bro room"));
            addDefaultPlayers();
            startNewRoundForRoom(1);
            startNewRoundForRoom(1);
        } catch (RoomException e) {
            e.printStackTrace();
        }
        */
    }

    /**
     * !!! WARING !!!
     * TODO: remove method. Only use for testing purposes.
     */
    private void addDefaultPlayers() throws RoomException {
        savePlayer(1, 1);
        savePlayer(1, 2);
        savePlayer(1, 3);
        savePlayer(1, 4);
        savePlayer(1, 5);
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
    public Player savePlayer(int roomId, int userId) throws RoomException {
        //Get room
        Room room = getRoom(roomId);

        //Determine if room is full
        if (room.getPlayersInRoom().size() > room.getGameRules().getMaxPlayerCount())
            throw new RoomException(RoomServiceImpl.class, "Maximum player capacity is reached.");

        //Add player to room
        Player player = new Player(room.getGameRules().getStartingChips(), userId);
        room.addPlayer(player);
        saveRoom(room);
        return player;
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
        Round prevRound = room.getCurrentRound();
        if (players.size() < 2)
            throw new RoomException(RoomServiceImpl.class, "There must be at least 2 players int room to start a round.");
        int button = room.getRounds().size() == 0 ? 0 : prevRound.getButton();

        //Create new round
        Round round = roundService.startNewRound(room.getPlayersInRoom(), button);
        if (room.getRounds().size() > 0) prevRound.setFinished(true);
        room.addRound(round);
        saveRoom(room);
        return round;
    }

    /**
     * The rooms returned by this method are cached locally.
     * @return An unmodifiable collection of all the rooms from the database.
     */
    @Override
    @Cacheable(value = "rooms")
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
    @Cacheable(value = "room")
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