package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.UserDto;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.shared.TokenDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Controller
@RequiredArgsConstructor
public class RoomSocketController {
    private final SimpMessagingTemplate template;
    private final ModelMapper modelMapper;
    private final RoomService roomService;
    private final String LOCALUSERURL = "http://localhost:5000/api/user";
    private final String USERURL = "https://poker-user-service.herokuapp.com/api/user";

    /**
     * If a player joins a room, it is received here. All players in the same room will be notified.
     */
    @MessageMapping("/rooms/{roomId}/join")
    public void joinRoom(@DestinationVariable("roomId") int roomId, PlayerDTO playerDTO) throws RoomException {
        UserDto userDto = getUser(playerDTO.getAccess_token());
        userDto.setChips(userDto.getChips() - roomService.checkChips(roomId, userDto.getChips()));
        if (updateUser(playerDTO.getAccess_token(), userDto) != null) {
            Player playerIn = roomService.joinRoom(roomId, playerDTO.getUserId());
            PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
            this.template.convertAndSend("/room/join/" + roomId, playerOut);
        }
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

    private UserDto getUser(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(LOCALUSERURL, HttpMethod.GET, entity, UserDto.class).getBody();
    }

    private UserDto updateUser(String token, UserDto userDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto, headers);

        return restTemplate.exchange(LOCALUSERURL, HttpMethod.PUT, entity, UserDto.class).getBody();
    }
}
