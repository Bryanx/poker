package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.chat.MessageDTO;
import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.UserDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import be.kdg.gameservice.shared.UserApiGateway;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * This API is used for API connections that have somthing to do
 * with the games of poker
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoundApiController {
    private static final String ID_KEY = "uuid";
    private final ModelMapper modelMapper;
    private final RoundService roundService;
    private final RoomService roomService;
    private final UserApiGateway userApiGateway;
    private final SimpMessagingTemplate template;

    /**
     * Gets all the possible acts that can be played for a specific player
     * in a specific round.
     *
     * @param roundId The id of the round
     * @return Status code 200 if the get succeeded.
     * @throws RoundException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rounds/{roundId}/possible-acts")
    public ResponseEntity<ActType[]> getPossibleActs(@PathVariable int roundId,
                                                     OAuth2Authentication authentication) throws RoundException {
        List<ActType> actTypes = roundService.getPossibleActs(roundId, userApiGateway.getUserInfo(authentication).get(ID_KEY).toString());
        return new ResponseEntity<>(modelMapper.map(actTypes, ActType[].class), HttpStatus.OK);
    }

    /**
     * Saves an act that is played by a player in the back end.
     * The act is validated in the round service for a last time.
     * The players act will than be sent to the rest of the room.
     * If the round has ended then a winner will be broad-casted.
     * The current round will be broad-casted.
     * If the round has ended then a new round will be broad-casted.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/rounds/act")
    public ResponseEntity<ActDTO> addAct(@RequestBody @Valid ActDTO actDTO, OAuth2Authentication authentication) throws RoundException, RoomException {
        this.roundService.saveAct(actDTO.getRoundId(), actDTO.getUserId(),
                actDTO.getType(), actDTO.getPhase(), actDTO.getBet(), actDTO.isAllIn());

        Optional<Player> winnerOptional = roundService.checkEndOfRound(actDTO.getRoundId());
        Optional<Player> playerOptional = roundService.checkFolds(actDTO.getRoundId());
        Round round;
        RoundDTO roundOut;
        if (winnerOptional.isPresent() || playerOptional.isPresent()) {
            Player winner = winnerOptional.orElseGet(playerOptional::get);
            userApiGateway.addWin(winner.getUserId());
            sendWinMessages(authentication, winner, actDTO);
            this.template.convertAndSend("/room/receive-winner/" + actDTO.getRoomId(), modelMapper.map(winner, PlayerDTO.class));
            round = roomService.startNewRoundForRoom(actDTO.getRoomId());
            roundOut = modelMapper.map(round, RoundDTO.class);

            List<String> userIds = round.getPlayersInRound().stream().map(Player::getUserId).collect(collectingAndThen(toList(), Collections::unmodifiableList));
            userApiGateway.addGamesPlayed(userIds);
        } else {
            actDTO.setNextUserId(roundService.determineNextUserId(actDTO.getRoundId(), actDTO.getUserId()));
            this.template.convertAndSend("/room/receive-act/" + actDTO.getRoomId(), actDTO);
            round = roomService.getCurrentRound(actDTO.getRoomId());
            roundOut = modelMapper.map(round, RoundDTO.class);
        }

        this.template.convertAndSend("/room/receive-round/" + actDTO.getRoomId(), roundOut);

        return new ResponseEntity<>(actDTO, HttpStatus.CREATED);
    }

    private void sendWinMessages(OAuth2Authentication authentication, Player winner, ActDTO actDTO) {
        String token = userApiGateway.getTokenFromAuthentication(authentication);
        UserDTO user = userApiGateway.getUser(token, actDTO.getUserId());
        String username = user.getUsername();
        String winnerString = String.format("Winner is: %s with %s", username, winner.getHandType().name());
        MessageDTO winnerMsg = new MessageDTO("system", winnerString);
        this.template.convertAndSend("/chatroom/receive/" + actDTO.getRoomId(), winnerMsg);
    }

}