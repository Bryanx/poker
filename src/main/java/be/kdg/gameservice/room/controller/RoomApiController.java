package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Round;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This API is used for managing all the rooms.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public final class RoomApiController {
    private final ModelMapper modelMapper;
    private final RoomService roomService;

    /**
     * @return Statuscode 200 with all the rooms.
     */
    @GetMapping("/rooms")
    public ResponseEntity<RoomDTO[]> getRooms() {
        List<Room> roomsIn = roomService.getRooms();
        RoomDTO[] roomsOut = modelMapper.map(roomsIn, RoomDTO[].class);

        return new ResponseEntity<>(roomsOut, HttpStatus.OK);
    }

    /**
     * @param roomId The id of the room that needs be retrieved.
     * @return Status code 200 with the corresponding room object.
     * @throws RoomException Rerouted to handler.
     */
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable int roomId) throws RoomException {
        Room roomIn = roomService.getRoom(roomId);
        RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.OK);
    }

    /**
     * Gives back the current round of the room.
     *
     * @param roomId The id of the room from where the rounds needs to be returned/created.
     * @return Status 200.
     * @throws RoomException Rerouted to handler.
     */
    @GetMapping("/rooms/{roomId}/rounds/current-round")
    public ResponseEntity<RoundDTO> getCurrentRound(@PathVariable int roomId) throws RoomException {
        Round roundIn = roomService.getCurrentRound(roomId);
        RoundDTO roundOut = modelMapper.map(roundIn, RoundDTO.class);
        return new ResponseEntity<>(roundOut, HttpStatus.OK);
    }

    /**
     * This API will be called when a player specific to join a specific room.
     *
     * @param roomId The id of the room the player wants to join.
     * @param userId The id of the user instance
     * @return Status code 201 if the player joined the room successfully.
     * @throws RoomException Rerouted to handler.
     */
    @PostMapping("/rooms/{roomId}/players/{userId}")
    public ResponseEntity<PlayerDTO> savePlayer(@PathVariable int roomId, @PathVariable int userId) throws RoomException {
        Player playerIn = roomService.savePlayer(roomId, userId);
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);

        return new ResponseEntity<>(playerOut, HttpStatus.CREATED);
    }

    /**
     * This API will be called when a round has finished and a new round needs to start.
     *
     * @param roomId The id of the room were a new round needs to be created for.
     * @return Status 200.
     * @throws RoomException Rerouted to handler.
     */
    @PostMapping("/rooms/{roomId}/start-new-round")
    public ResponseEntity<RoundDTO> startNewRound(@PathVariable int roomId) throws RoomException {
        Round roundIn = roomService.startNewRoundForRoom(roomId);
        RoundDTO roundOut = modelMapper.map(roundIn, RoundDTO.class);

        return new ResponseEntity<>(roundOut, HttpStatus.CREATED);
    }
}
