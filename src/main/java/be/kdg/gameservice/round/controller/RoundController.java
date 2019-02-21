package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoundController {
    private final SimpMessagingTemplate template;
    private final RoundService roundService;
    private final RoomService roomService;
    private final ModelMapper modelMapper;

    /**
     * If a player choses an act in a room, it is received here.
     * The players choice will than be sent to the rest of the room.
     */
    @MessageMapping("/rooms/{roomId}/sendact")
    public void onReceiveAct(ActDTO actDTO, @DestinationVariable("roomId") int roomId) throws RoundException, RoomException {
        this.template.convertAndSend("/room/receiveact/" + roomId, actDTO);

        this.roundService.saveAct(actDTO.getRoundId(), actDTO.getUserId(),
                actDTO.getType(), actDTO.getPhase(), actDTO.getBet());

        Round round = roomService.getCurrentRound(roomId);
        RoundDTO roundOut = modelMapper.map(round, RoundDTO.class);

        this.template.convertAndSend("/room/receive-round/" + roomId, roundOut);
    }
}
