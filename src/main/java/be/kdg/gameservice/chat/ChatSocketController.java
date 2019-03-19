package be.kdg.gameservice.chat;

import be.kdg.gameservice.shared.BaseController;
import be.kdg.gameservice.shared.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatSocketController extends BaseController {
    private final SimpMessagingTemplate template;

    /**
     * If a player sends a chat message to a room, it is received here.
     * The message is then send to all players in the same room.
     */
    @MessageMapping("/chatrooms/{roomId}/send")
    public void onReceiveMessage(MessageDTO message, @DestinationVariable("roomId") String roomId) {
        logIncomingCall("onReceiveMessage");
        this.template.convertAndSend("/chatroom/receive/" + roomId, message);
    }
}