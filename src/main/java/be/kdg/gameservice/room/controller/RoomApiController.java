package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.controller.dto.UserDto;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.PlayerService;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.shared.config.WebConfig;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * This API is used for managing all the rooms.
 */
@RestController
@RequestMapping("/api")
public class RoomApiController {
    private static final String ID_KEY = "uuid";
    private final String USER_SERVICE_URL;

    private final ResourceServerTokenServices resourceTokenServices;
    private final ModelMapper modelMapper;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final SimpMessagingTemplate template;

    @Autowired
    public RoomApiController(ResourceServerTokenServices resourceTokenServices,
                             ModelMapper modelMapper,
                             RoomService roomService,
                             SimpMessagingTemplate template,
                             WebConfig webConfig,
                             PlayerService playerService) {
        this.resourceTokenServices = resourceTokenServices;
        this.modelMapper = modelMapper;
        this.roomService = roomService;
        this.template = template;
        this.USER_SERVICE_URL = webConfig.getUserServiceUrl();
        this.playerService = playerService;
    }

    /**
     * @return Status code 200 with all the rooms from the database.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms")
    public ResponseEntity<RoomDTO[]> getRooms() {
        List<Room> roomsIn = roomService.getRooms();
        RoomDTO[] roomsOut = modelMapper.map(roomsIn, RoomDTO[].class);
        return new ResponseEntity<>(roomsOut, HttpStatus.OK);
    }

    /**
     * @param roomId The id of the room that needs be retrieved.
     * @return Status code 200 with the corresponding room object.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable int roomId) throws RoomException {
        Room roomIn = roomService.getRoom(roomId);
        RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.OK);
    }

    /**
     * @param roomDTO The request body that contains the name and the rules fot the room.
     * @return Status code 201 with the newly created room.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/rooms")
    public ResponseEntity<RoomDTO> addRoom(@RequestBody @Valid RoomDTO roomDTO) {
        Room room = roomService.addRoom(roomDTO.getName(), roomDTO.getGameRules());
        RoomDTO roomOut = modelMapper.map(room, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.CREATED);
    }

    /**
     * @param roomId The id of the room that needs to be updated.
     * @param roomDTO The DTO that contains the updated data.
     * @return Status code 202 if the room was successfully updated.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDTO> changeRoom(@PathVariable int roomId, @RequestBody @Valid RoomDTO roomDTO) throws RoomException {
        Room room = roomService.changeRoom(roomId, modelMapper.map(roomDTO, Room.class));
        RoomDTO roomOut = modelMapper.map(room, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.ACCEPTED);
    }

    /**
     * @param roomId The id of the room that needs to be deleted.
     * @return Status code 202 if the room was successful deleted.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int roomId) throws RoomException {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    /**
     * @param roomId         The id of the room.
     * @param authentication Needed for retrieving the userId.
     * @return Status code 202 if the player was successfully deleted from the room.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/rooms/{roomId}/leave-room")
    public ResponseEntity<PlayerDTO> leaveRoom(@PathVariable int roomId, OAuth2Authentication authentication) throws RoomException, RoundException {
        Player player = playerService.leaveRoom(roomId, getUserInfo(authentication).get(ID_KEY).toString());

        roomService.enoughRoundPlayers(roomId);

        String token = getTokenFromAuthentication(authentication);
        UserDto userDto = getUser(token);
        userDto.setChips(userDto.getChips() + player.getChipCount());

        if (updateUser(token, userDto) != null) {
            Room roomIn = roomService.getRoom(roomId);
            RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);
            this.template.convertAndSend("/room/receive-room/" + roomId, roomOut);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/rooms/players")
    public ResponseEntity<PlayerDTO> changePlayer(@RequestBody @Valid PlayerDTO playerDTO) {
        Player playerIn = playerService.savePlayer(modelMapper.map(playerDTO, Player.class));
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms/players")
    public ResponseEntity<PlayerDTO> getPlayer(OAuth2Authentication authentication) {
        Player playerIn = playerService.getPlayer(getUserInfo(authentication).get(ID_KEY).toString());
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.OK);
    }

    /**
     * If a player joins a room, it is received here.
     * The user service will be used to check if the user has enough chips.
     * If that is the case then the chips will be transferred to the player.
     * All players in the same room will be notified.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms/{roomId}/join")
    public ResponseEntity<PlayerDTO> joinRoom(@PathVariable int roomId, OAuth2Authentication authentication) throws RoomException {
        String token = getTokenFromAuthentication(authentication);
        UserDto userDto = getUser(token);
        userDto.setChips(userDto.getChips() - roomService.checkChips(roomId, userDto.getChips()));
        if (updateUser(token, userDto) != null) {
            Player playerIn = playerService.joinRoom(roomId, getUserInfo(authentication).get(ID_KEY).toString());
            PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
            Room roomIn = roomService.getRoom(roomId);
            RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);
            this.template.convertAndSend("/room/receive-room/" + roomId, roomOut);
            return new ResponseEntity<>(playerOut, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * When a player joins a room or when a round has ended will this method check
     * if a new round can be initiated, if it's possible a new round will be sent to it's players.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms/{roomId}/current-round")
    public void getCurrentRound(@PathVariable int roomId) throws RoomException {
        Round round = roomService.getCurrentRound(roomId);
        RoundDTO roundOut = modelMapper.map(round, RoundDTO.class);
        this.template.convertAndSend("/room/receive-round/" + roomId, roundOut);
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    private Map<String, Object> getUserInfo(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
    }

    private String getTokenFromAuthentication(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return oAuth2AuthenticationDetails.getTokenValue();
    }

    private UserDto getUser(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        return restTemplate.exchange(USER_SERVICE_URL, HttpMethod.GET, entity, UserDto.class).getBody();
    }

    private UserDto updateUser(String token, UserDto userDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<UserDto> entity = new HttpEntity<>(userDto, headers);

        return restTemplate.exchange(USER_SERVICE_URL, HttpMethod.PUT, entity, UserDto.class).getBody();
    }
}
