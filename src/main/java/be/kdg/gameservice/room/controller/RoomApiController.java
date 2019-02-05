package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.service.api.RoomService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomApiController {
    private final ModelMapper modelMapper;
    private final RoomService roomService;

    @Autowired
    public RoomApiController(ModelMapper modelMapper, RoomService roomService) {
        this.modelMapper = modelMapper;
        this.roomService = roomService;
    }

    //TODO: proper exception handling.
    @PostMapping("/rooms/{roomId}/players/{name}")
    public ResponseEntity<PlayerDTO> savePlayer(@PathVariable int roomId, @PathVariable String name) throws RoomException {
        Player playerIn = roomService.savePlayer(roomId, name);
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);

        return new ResponseEntity<>(playerOut, HttpStatus.CREATED);
    }
}
