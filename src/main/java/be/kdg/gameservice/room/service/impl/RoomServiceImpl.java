package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.GameRules;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * This service will be used to manage the ongoing activity of a specific room.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoundService roundService;

    //TODO: maarten, remove this method if only used in tests.
    /**
     * Returns room based on roomName
     * Carefull, roomname should be unique.
     *
     * @param roomName The name of the room we need to search for.
     * @return The room
     */
    @Override
    public Room getRoomByName(String roomName) {
        return roomRepository.getRoomByName(roomName);
    }

    /**
     * @param roomId The room the new round needs to be created for.
     * @return The updated room with the new round.
     * @throws RoomException Thrown if there are less than 2 players in the round.
     */
    @Override
    public Round startNewRoundForRoom(int roomId) throws RoomException, RoundException {
        //Get room
        Room room = getRoom(roomId);

        //Determine if round can be created
        List<Player> players = room.getPlayersInRoom();
        players.forEach(player -> player.setAllIn(false));
        if (players.size() < 2)
            throw new RoomException(RoomServiceImpl.class, "There must be at least 2 players int room to start a round.");
        int button = room.getRounds().size() == 0 ? 0 : room.getCurrentRound().getButton();

        //Create new round
        Round round = roundService.startNewRound(room.getPlayersInRoom(), button);
        if (room.getRounds().size() > 0) room.getCurrentRound().setFinished(true);
        room.addRound(round);
        saveRoom(room);

        Round roundFromDB = getCurrentRound(roomId);
        roundService.playBlinds(roundFromDB, room.getGameRules().getSmallBlind(), room.getGameRules().getBigBlind());
        return roundFromDB;
    }

    /**
     * The rooms returned by this method are cached locally.
     *
     * @return An unmodifiable collection of all the rooms from the database.
     */
    @Override
    public <T extends Room> List<Room> getRooms(Class<T> aClass) {
        return roomRepository.findAll().stream()
                .filter(room -> room.getClass().getSimpleName().equals(aClass.getSimpleName()))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Gets the current round of a specific room, if the round does not exist,
     * than a new round will be created.
     *
     * @return The current round of the room.
     */
    @Override
    public Round getCurrentRound(int roomId) throws RoomException, RoundException {
        //Get data
        Room room = getRoom(roomId);

        //Determine which round to give back
        if (room.getRounds().size() <= 0 || room.getCurrentRound().isFinished()) return startNewRoundForRoom(roomId);
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
     * Adds a room to the database.
     *
     * @param name      The name of the room.
     * @param gameRules The rules that will be applied in this room.
     * @return The newly created room.
     */
    @Override
    public Room addRoom(String name, GameRules gameRules) {
        Room room = new Room(gameRules, name);
        return saveRoom(room);
    }

    /**
     * Changes a room in the database.
     *
     * @param room The room with adjusted values that needs to be updated.
     * @return The updated room.
     */
    @Override
    public Room changeRoom(int roomId, Room room) throws RoomException {
        //Get data
        Room roomToUpdate = getRoom(roomId);

        //Update room
        roomToUpdate.setName(room.getName());
        roomToUpdate.setGameRules(room.getGameRules());
        return saveRoom(roomToUpdate);
    }

    /**
     * Deletes a room from database.
     *
     * @param id The id of the room that needs to be deleted.
     */
    @Override
    public void deleteRoom(int id) {
        roomRepository.deleteById(id);
    }

    /**
     * @param room The room that needs to be updated or saved.
     */
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Checks if the user has enough chips to join a room.
     */
    @Override
    public int checkChips(int roomId, int userChips) throws RoomException {
        //Get data
        Room room = getRoom(roomId);

        //Do check
        if (room.getGameRules().getStartingChips() > userChips)
            throw new RoomException(RoomServiceImpl.class, "User does not have enough chips to join.");

        return room.getGameRules().getStartingChips();
    }

    /**
     * Checks if there are enough players left to play.
     * If this is not the case then the last player will receive the pot.
     */
    @Override
    public void enoughRoundPlayers(int roomId) throws RoomException, RoundException {
        Room room = getRoom(roomId);

        if (room.getRounds().size() > 0) {
            if (!room.getCurrentRound().isFinished() && room.getCurrentRound().getPlayersInRound().size() < 2) {
                room.getCurrentRound().setFinished(true);
                roundService.distributeCoins(room.getCurrentRound().getId(), room.getCurrentRound().getPlayersInRound().get(0));
            }
        }
    }
}