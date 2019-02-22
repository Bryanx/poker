package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.model.Round;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class RoomSocketController {
    private final SimpMessagingTemplate template;
    private final ModelMapper modelMapper;
    private final RoomService roomService;

    /**
     * If a player joins a room, it is received here. All players in the same room will be notified.
     */
    @MessageMapping("/rooms/{roomId}/join")
    public void joinRoom(@DestinationVariable("roomId") int roomId, PlayerDTO playerDTO) throws RoomException {
        Player playerIn = roomService.joinRoom(roomId, playerDTO.getUserId());
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        this.template.convertAndSend("/room/join/" + roomId, playerOut);
    }

    /**
     * When a player joins a gameroom or when a round has ended will this method check
     * if a new round can be initiated, if it's possible a new round will be sent to it's players.
     */
    @MessageMapping("/rooms/{roomId}/get-current-round")
    public void getCurrentRound(@DestinationVariable("roomId") int roomId) throws RoomException {
        Round round = roomService.getCurrentRound(roomId);
        RoundDTO roundOut = modelMapper.map(round, RoundDTO.class);
        this.template.convertAndSend("/room/receive-round/" + roomId, roundOut);
    }
}
