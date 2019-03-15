package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.replay.dto.ReplayDTO;
import be.kdg.gameservice.replay.model.Replay;
import be.kdg.gameservice.replay.service.api.ReplayService;
import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.PrivateRoomDTO;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.controller.dto.UserDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.PrivateRoom;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.PlayerService;
import be.kdg.gameservice.room.service.api.PrivateRoomService;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.shared.BaseController;
import be.kdg.gameservice.shared.UserApiGateway;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * This API is used for managing all the rooms.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomApiController extends BaseController {
    private final UserApiGateway userApiGateway;
    private final ModelMapper modelMapper;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final ReplayService replayService;
    private final PrivateRoomService privateRoomService;
    private final SimpMessagingTemplate template;

    /**
     * @return Status code 200 with all the rooms from the database.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms")
    public ResponseEntity<RoomDTO[]> getRooms() {
        logIncomingCall("getRooms");
        List<Room> roomsIn = roomService.getRooms(Room.class);
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
        logIncomingCall("getRoom");
        Room roomIn = roomService.getRoom(roomId);
        RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.OK);
    }

    /**
     * Gives back all the replays of a specific user.
     *
     * @param authentication The token used for retrieving the userId.
     * @return Status code 200 with the correct replays.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms/replays")
    public ResponseEntity<ReplayDTO[]> getReplays(OAuth2Authentication authentication) {
        logIncomingCall("getReplays");
        List<Replay> replays = replayService.getReplays(getUserId(authentication));
        ReplayDTO[] replaysOut = modelMapper.map(replays, ReplayDTO[].class);
        return new ResponseEntity<>(replaysOut, HttpStatus.OK);
    }

    /**
     * Gets a player by the correct user id;
     *
     * @param authentication The token used for retrieving the userId.
     * @return Status code 200 with the correct player.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms/players")
    public ResponseEntity<PlayerDTO> getPlayer(OAuth2Authentication authentication) throws RoomException {
        logIncomingCall("getPlayer");
        Player playerIn = playerService.getPlayer(getUserId(authentication));
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.OK);
    }

    /**
     * Gives back all private rooms that the user is authenticated for.
     *
     * @param authentication The token used for retrieving the userId.
     * @return Status code 200 with all the private rooms.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms/private")
    public ResponseEntity<PrivateRoomDTO[]> getPrivateRooms(OAuth2Authentication authentication) {
        logIncomingCall("getPrivateRooms");
        List<PrivateRoom> privateRooms = privateRoomService.getPrivateRooms(getUserId(authentication));
        PrivateRoomDTO[] privateRoomOut = modelMapper.map(privateRooms, PrivateRoomDTO[].class);
        return new ResponseEntity<>(privateRoomOut, HttpStatus.OK);
    }

    /**
     * Gives back all private rooms that a specific user owns.
     *
     * @param authentication The token used for retrieving the userId.
     * @return Status code 200 with all the private rooms.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms/private/owner")
    public ResponseEntity<PrivateRoomDTO[]> getPrivateRoomsFromOwner(OAuth2Authentication authentication) {
        logIncomingCall("getPrivateRoomsFromOwner");
        List<PrivateRoom> privateRooms = privateRoomService.getPrivateRoomsFromOwner(getUserId(authentication));
        PrivateRoomDTO[] privateRoomOut = modelMapper.map(privateRooms, PrivateRoomDTO[].class);
        return new ResponseEntity<>(privateRoomOut, HttpStatus.OK);
    }

    /**
     * Retrieves a private room from the database. This will only happen if the user
     * has the right credentials.
     *
     * @param roomId         The id of the room.
     * @param authentication The token used for retrieving the userId.
     * @return Status code 200 with the requested room.
     * @throws RoomException Rerouted to handler
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/rooms/private/{roomId}")
    public ResponseEntity<PrivateRoomDTO> getPrivateRoom(@PathVariable int roomId, OAuth2Authentication authentication) throws RoomException {
        logIncomingCall("getPrivateRoom");
        PrivateRoom privateRoom = privateRoomService.getPrivateRoom(roomId, getUserId(authentication));
        PrivateRoomDTO privateRoomOut = modelMapper.map(privateRoom, PrivateRoomDTO.class);
        return new ResponseEntity<>(privateRoomOut, HttpStatus.OK);
    }

    /**
     * If a player joins a room, it is received here.
     * The user service will be used to check if the user has enough chips.
     * If that is the case then the chips will be transferred to the player.
     * All players in the same room will be notified.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms/{roomId}/join")
    public synchronized ResponseEntity<PlayerDTO> joinRoom(@PathVariable int roomId, OAuth2Authentication authentication) throws RoomException {
        logIncomingCall("joinRoom");

        String token = userApiGateway.getTokenFromAuthentication(authentication);
        UserDTO userDto = userApiGateway.getUser(token, "");
        userDto.setChips(userDto.getChips() - roomService.checkChips(roomId, userDto.getChips()));
        Player playerIn = playerService.joinRoom(roomId, getUserId(authentication));

        if (userApiGateway.updateUser(token, userDto) != null && playerIn != null) {
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
    public void getCurrentRound(@PathVariable int roomId) throws RoomException, RoundException {
        logIncomingCall("getCurrentRound");
        Round round = roomService.getCurrentRound(roomId);

        if (round.getCurrentPhase() == Phase.PRE_FLOP) {
            List<String> userIds = round.getPlayersInRound().stream().map(Player::getUserId).collect(collectingAndThen(toList(), Collections::unmodifiableList));
            userApiGateway.addGamesPlayed(userIds);
        }

        RoundDTO roundOut = modelMapper.map(round, RoundDTO.class);
        this.template.convertAndSend("/room/receive-round/" + roomId, roundOut);
    }

    /**
     * @param roomDTO The request body that contains the name and the rules fot the room.
     * @return Status code 201 with the newly created room.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/rooms")
    public ResponseEntity<RoomDTO> addRoom(@RequestBody @Valid RoomDTO roomDTO) {
        logIncomingCall("addRoom");
        Room room = roomService.addRoom(roomDTO.getName(), roomDTO.getGameRules());
        RoomDTO roomOut = modelMapper.map(room, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.CREATED);
    }

    /**
     * Creates a private room for a specific user and adds that user
     * automatically to the whitelist.
     *
     * @param privateRoomDTO All the data of the room.
     * @param authentication The token used for retrieving the userId.
     * @return Status code 201 with the newly created room.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/rooms/private")
    public ResponseEntity<PrivateRoomDTO> addPrivateRoom(@RequestBody PrivateRoomDTO privateRoomDTO, OAuth2Authentication authentication) {
        logIncomingCall("addPrivateRoom");
        PrivateRoom privateRoomIn = privateRoomService.addPrivateRoom(getUserId(authentication), privateRoomDTO.getGameRules(), privateRoomDTO.getName());
        PrivateRoomDTO privateRoomOut = modelMapper.map(privateRoomIn, PrivateRoomDTO.class);
        return new ResponseEntity<>(privateRoomOut, HttpStatus.CREATED);
    }

    /**
     * @param roomId  The id of the room that needs to be updated.
     * @param roomDTO The DTO that contains the updated data.
     * @return Status code 202 if the room was successfully updated.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<RoomDTO> changeRoom(@PathVariable int roomId, @RequestBody @Valid RoomDTO roomDTO) throws RoomException {
        logIncomingCall("changeRoom");
        Room room = roomService.changeRoom(roomId, modelMapper.map(roomDTO, Room.class));
        RoomDTO roomOut = modelMapper.map(room, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.ACCEPTED);
    }

    /**
     * @param playerDTO The player DTO that
     * @return status code 202 if the player was successfully updated in the database.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PutMapping("/rooms/players")
    public ResponseEntity<PlayerDTO> changePlayer(@RequestBody @Valid PlayerDTO playerDTO) {
        logIncomingCall("changePlayer");
        Player playerIn = playerService.savePlayer(modelMapper.map(playerDTO, Player.class));
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.ACCEPTED);
    }

    /**
     * Removes a player from the white list
     *
     * @param roomId The id of the room
     * @param userId The id of the user that needs to be removed from the whitelist.
     * @return Status code 202.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/rooms/private/{roomId}/remove-user/{userId}")
    public ResponseEntity<PrivateRoomDTO> removeFromWhitelist(@PathVariable int roomId, @PathVariable String userId) throws RoomException {
        logIncomingCall("removeFromWhitelist");
        PrivateRoom privateRoom = privateRoomService.removeUserFromWhiteList(roomId, userId);
        PrivateRoomDTO privateRoomOut = modelMapper.map(privateRoom, PrivateRoomDTO.class);
        return new ResponseEntity<>(privateRoomOut, HttpStatus.ACCEPTED);
    }

    /**
     * Adds a player from the white list
     *
     * @param roomId The id of the room
     * @param userId The id of the user that needs to be removed from the whitelist.
     * @return Status code 202.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PatchMapping("/rooms/private/{roomId}/add-user/{userId}")
    public ResponseEntity<PrivateRoomDTO> addToWhitelist(@PathVariable int roomId, @PathVariable String userId) throws RoomException {
        logIncomingCall("addToWhitelist");
        PrivateRoom privateRoom = privateRoomService.addUserToWhiteList(roomId, userId);
        PrivateRoomDTO privateRoomOut = modelMapper.map(privateRoom, PrivateRoomDTO.class);
        return new ResponseEntity<>(privateRoomOut, HttpStatus.ACCEPTED);
    }

    /**
     * @param roomId The id of the room that needs to be deleted.
     * @return Status code 202 if the room was successful deleted.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int roomId) throws RoomException {
        logIncomingCall("deleteRoom");
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
        logIncomingCall("leaveRoom");
        Player player = playerService.leaveRoom(roomId, getUserId(authentication));

        roomService.enoughRoundPlayers(roomId);

        String token = userApiGateway.getTokenFromAuthentication(authentication);
        UserDTO userDto = userApiGateway.getUser(token, "");
        userDto.setChips(userDto.getChips() + player.getChipCount());

        if (userApiGateway.updateUser(token, userDto) != null) {
            Room roomIn = roomService.getRoom(roomId);
            RoomDTO roomOut = modelMapper.map(roomIn, RoomDTO.class);
            this.template.convertAndSend("/room/receive-room/" + roomId, roomOut);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
