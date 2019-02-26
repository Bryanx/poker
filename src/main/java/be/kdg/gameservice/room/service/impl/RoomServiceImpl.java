package be.kdg.gameservice.room.service.impl;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.*;
import be.kdg.gameservice.room.persistence.PlayerRepository;
import be.kdg.gameservice.room.persistence.RoomRepository;
import be.kdg.gameservice.room.persistence.WhiteListedPlayerRepository;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This service will be used to manage the ongoing activity of a specific room.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final WhiteListedPlayerRepository whiteListedPlayerRepository;
    private final RoundService roundService;

    /**
     * used for quality check
     * TODO: remove this when everything works properly.
     */
    @PostConstruct
    public void testData() {
        //make public room
        roomRepository.save(new Room(new GameRules(), "test room"));

        //make private room
        WhiteListedUser whiteListedPlayer1 = new WhiteListedUser("1");
        WhiteListedUser whiteListedPlayer2 = new WhiteListedUser("2");
        WhiteListedUser whiteListedPlayer3 = new WhiteListedUser("3");

        PrivateRoom room = new PrivateRoom("test private room", "1");
        room.addWhiteListedPlayer(whiteListedPlayer1);
        room.addWhiteListedPlayer(whiteListedPlayer2);
        room.addWhiteListedPlayer(whiteListedPlayer3);

        roomRepository.save(room);
        whiteListedPlayerRepository.delete(whiteListedPlayer1);
    }

    /**
     * Creates a new room based on room object passed to roomRepository save method
     *
     * @param room
     * @return
     */
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Returns room based on roomName
     * Carefull, roomname should be unique.
     *
     * @param roomName
     * @return
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
        return getCurrentRound(roomId);
    }

    /**
     * The rooms returned by this method are cached locally.
     *
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
     * @param name        The name of the room.
     * @param gameRulesIn The rules that will be applied in this room.
     * @return The newly created room.
     */
    @Override
    public Room addRoom(String name, GameRules gameRulesIn) {
        GameRules gameRulesOut = new GameRules(
                gameRulesIn.getSmallBlind(),
                gameRulesIn.getBigBlind(),
                gameRulesIn.getPlayDelay(),
                gameRulesIn.getStartingChips(),
                gameRulesIn.getMaxPlayerCount());
        Room room = new Room(gameRulesOut, name);
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
        roomToUpdate.getGameRules().setSmallBlind(room.getGameRules().getSmallBlind());
        roomToUpdate.getGameRules().setBigBlind(room.getGameRules().getBigBlind());
        roomToUpdate.getGameRules().setMaxPlayerCount(room.getGameRules().getMaxPlayerCount());
        roomToUpdate.getGameRules().setPlayDelay(room.getGameRules().getPlayDelay());
        roomToUpdate.getGameRules().setStartingChips(room.getGameRules().getStartingChips());
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
        Room room = getRoom(roomId);
        if (room.getGameRules().getStartingChips() > userChips) {
            throw new RoomException(RoomServiceImpl.class, "User does not have enough chips to join.");
        }
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