package be.kdg.gameservice.room.controller;

import be.kdg.gameservice.room.controller.dto.PlayerDTO;
import be.kdg.gameservice.room.controller.dto.RoomDTO;
import be.kdg.gameservice.room.exception.RoomException;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.room.service.api.RoomService;
import be.kdg.gameservice.round.controller.dto.RoundDTO;
import be.kdg.gameservice.round.model.Round;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * This API is used for managing all the rooms.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RoomApiController {
    private static final String ID_KEY = "uuid";
    private final ResourceServerTokenServices resourceTokenServices;
    private final ModelMapper modelMapper;
    private final RoomService roomService;

    /**
     * @return Status code 200 with all the rooms from the database.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/rooms")
    public ResponseEntity<RoomDTO> addRoom(@RequestBody @Valid RoomDTO roomDTO) {
        Room room = roomService.addRoom(roomDTO.getName(), roomDTO.getGameRules());
        RoomDTO roomOut = modelMapper.map(room, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.CREATED);
    }

    /**
     * @param roomDTO The DTO that contains the updated data.
     * @return Status code 202 if the room was successfully updated.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/rooms")
    public ResponseEntity<RoomDTO> changeRoom(@RequestBody @Valid RoomDTO roomDTO) throws RoomException {
        Room room = roomService.changeRoom(modelMapper.map(roomDTO, Room.class));
        RoomDTO roomOut = modelMapper.map(room, RoomDTO.class);
        return new ResponseEntity<>(roomOut, HttpStatus.ACCEPTED);
    }

    /**
     * @param roomId The id of the room that needs to be deleted.
     * @return Status code 202 if the room was successful deleted.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_AMDIN')")
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
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/rooms/{roomId}/leave-room")
    public ResponseEntity<PlayerDTO> leaveRoom(@PathVariable int roomId, OAuth2Authentication authentication) throws RoomException {
        Player playerIn = roomService.leaveRoom(roomId, getUserInfo(authentication).get(ID_KEY).toString());
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.ACCEPTED);
    }

    /**
     * This API will be called when a player specific to join a specific room.
     *
     * @param roomId         The id of the room the player wants to join.
     * @param authentication Needed for retrieving the userId.
     * @return Status code 201 if the player joined the room successfully.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/rooms/{roomId}/join-room")
    public ResponseEntity<PlayerDTO> joinRoom(@PathVariable int roomId, OAuth2Authentication authentication) throws RoomException {
        Player playerIn = roomService.joinRoom(roomId, getUserInfo(authentication).get(ID_KEY).toString());
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.CREATED);
    }

    /**
     * Gives back the current round of the room.
     *
     * @param roomId The id of the room from where the rounds needs to be returned/created.
     * @return Status 200.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms/{roomId}/current-round")
    public ResponseEntity<RoundDTO> getCurrentRound(@PathVariable int roomId) throws RoomException {
        Round roundIn = roomService.getCurrentRound(roomId);
        RoundDTO roundOut = modelMapper.map(roundIn, RoundDTO.class);
        return new ResponseEntity<>(roundOut, HttpStatus.OK);
    }

    /**
     * This API will be called when a round has finished and a new round needs to start.
     *
     * @param roomId The id of the room were a new round needs to be created for.
     * @return Status 200.
     * @throws RoomException Rerouted to handler.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/rooms/{roomId}/start-new-round")
    public ResponseEntity<RoundDTO> startNewRound(@PathVariable int roomId) throws RoomException {
        Round roundIn = roomService.startNewRoundForRoom(roomId);
        RoundDTO roundOut = modelMapper.map(roundIn, RoundDTO.class);
        return new ResponseEntity<>(roundOut, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/rooms/players")
    public ResponseEntity<PlayerDTO> changePlayer(@RequestBody @Valid PlayerDTO playerDTO) {
        Player playerIn = roomService.savePlayer(modelMapper.map(playerDTO, Player.class));
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/rooms/players")
    public ResponseEntity<PlayerDTO> getPlayer(OAuth2Authentication authentication) {
        Player playerIn = roomService.getPlayer(getUserInfo(authentication).get(ID_KEY).toString());
        PlayerDTO playerOut = modelMapper.map(playerIn, PlayerDTO.class);
        return new ResponseEntity<>(playerOut, HttpStatus.OK);
    }

    /**
     * @param authentication Needed as authentication.
     * @return Gives back the details of a specific user.
     */
    private Map<String, Object> getUserInfo(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
        return resourceTokenServices.readAccessToken(oAuth2AuthenticationDetails.getTokenValue()).getAdditionalInformation();
    }
}
