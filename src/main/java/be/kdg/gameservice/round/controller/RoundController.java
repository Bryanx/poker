package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.round.controller.dto.ActDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RoundController {
    private final SimpMessagingTemplate template;

    /**
     * If a player choses an act in a room, it is received here.
     * The players choice will than be sent to the rest of the room.
     */
    @MessageMapping("/gameroom/sendact/{roomId}")
    public void onReceiveAct(ActDTO actDTO, @DestinationVariable("roomId") String roomId) {
        this.template.convertAndSend("/gameroom/receiveact/" + roomId, actDTO);
    }
}
