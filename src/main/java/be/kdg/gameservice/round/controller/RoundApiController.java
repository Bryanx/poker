package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.service.api.RoundService;
import be.kdg.gameservice.shared.config.WebConfig;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * This API is used for API connections that have somthing to do
 * with the games of poker
 */

@RestController
@RequestMapping("/api")
public class RoundApiController {
    private static final String ID_KEY = "uuid";
    private final ResourceServerTokenServices resourceTokenServices;
    private final ModelMapper modelMapper;
    private final RoundService roundService;
    private final RoomService roomService;
    private final SimpMessagingTemplate template;
    private final String USER_SERVICE_URL;

    public RoundApiController(ResourceServerTokenServices resourceTokenServices, ModelMapper modelMapper, RoundService roundService, RoomService roomService, SimpMessagingTemplate template, WebConfig webConfig) {
        this.resourceTokenServices = resourceTokenServices;
        this.modelMapper = modelMapper;
        this.roundService = roundService;
        this.roomService = roomService;
        this.template = template;
        this.USER_SERVICE_URL = webConfig.getUserServiceUrl();
    }

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
        List<ActType> actTypes = roundService.getPossibleActs(roundId, getUserInfo(authentication).get(ID_KEY).toString());
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
    public ResponseEntity<ActDTO> addAct(@RequestBody @Valid ActDTO actDTO) throws RoundException, RoomException {
        this.roundService.saveAct(actDTO.getRoundId(), actDTO.getUserId(),
                actDTO.getType(), actDTO.getPhase(), actDTO.getBet(), actDTO.isAllIn());

        Optional<Player> playerOptional = roundService.checkEndOfRound(actDTO.getRoundId());
        Round round;
        RoundDTO roundOut;

        if (playerOptional.isPresent()) {
            addWin(playerOptional.get().getUserId());

            this.template.convertAndSend("/room/receive-winner/" + actDTO.getRoomId(), modelMapper.map(playerOptional.get(), PlayerDTO.class));
            round = roomService.startNewRoundForRoom(actDTO.getRoomId());
            roundOut = modelMapper.map(round, RoundDTO.class);

            List<String> userIds = round.getPlayersInRound().stream().map(Player::getUserId).collect(collectingAndThen(toList(), Collections::unmodifiableList));
            addGamesPlayed(userIds);
        } else {
            actDTO.setNextUserId(roundService.determineNextUserId(actDTO.getRoundId(), actDTO.getUserId()));
            this.template.convertAndSend("/room/receive-act/" + actDTO.getRoomId(), actDTO);
            round = roomService.getCurrentRound(actDTO.getRoomId());
            roundOut = modelMapper.map(round, RoundDTO.class);
        }

        this.template.convertAndSend("/room/receive-round/" + actDTO.getRoomId(), roundOut);

        return new ResponseEntity<>(actDTO, HttpStatus.CREATED);
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    private Map<String, Object> getUserInfo(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
    }

    /**
     * Sends a rest template request to the user-service to increase the user his wins.
     */
    private void addWin(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(userId, headers);

        System.out.println(USER_SERVICE_URL + "/win" + " " + userId);
        restTemplate.exchange(USER_SERVICE_URL + "/win", HttpMethod.POST, entity, void.class);
    }

    /**
     * Sends a rest template request to the user-service to increase the user his gamesPlayed.
     */
    private void addGamesPlayed(List<String> userIds) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<List<String>> entity = new HttpEntity<>(userIds, headers);

        System.out.println(USER_SERVICE_URL + "/gamesplayed" + " " + userIds);
        restTemplate.exchange(USER_SERVICE_URL + "/gamesplayed", HttpMethod.POST, entity, void.class);
    }
}
