package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.service.api.RoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class RoomController {
    private final SimpMessagingTemplate template;
    private final ModelMapper modelMapper;
    private final RoomService roomService;

    /**
     * If a player joins a room, it is received here.
     */
    @MessageMapping("/rooms/{roomId}/join")
    public void join(@DestinationVariable("roomId") int roomId, PlayerDTO playerDTO) throws RoomException {
        Player playerIn = roomService.joinRoom(roomId, playerDTO.getUserId());
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        this.template.convertAndSend("/gameroom/join/" + roomId, playerOut);
    }
}
