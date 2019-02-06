package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return All the rooms from the database.
     */
    @GetMapping("/rooms")
    public ResponseEntity<RoomDTO[]> savePlayer() {
        List<Room> roomsIn = roomService.getRooms();
        RoomDTO[] roomsOut = modelMapper.map(roomsIn.toArray(), RoomDTO[].class);
        return new ResponseEntity<>(roomsOut, HttpStatus.OK);
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
     * TODO: give a round DTO instead of the room DTO
     */
    @PostMapping("/rooms/{roomId}/start-new-round")
    public ResponseEntity<RoomDTO> startNewRound(@PathVariable int roomId) throws RoomException {
        Room roomIn = roomService.startNewRoundForRoom(roomId);
        RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);

        return new ResponseEntity<>(roomOut, HttpStatus.OK);
    }
}
