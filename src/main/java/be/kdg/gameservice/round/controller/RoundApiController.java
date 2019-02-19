package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
 * This API is used for API connections that have somthing to do
 * with the games of poker
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class RoundApiController {
    private static final String ID_KEY = "uuid";
    private final ResourceServerTokenServices resourceTokenServices;
    private final ModelMapper modelMapper;
    private final RoundService roundService;

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
     *
     * @param actDTO The information needed to make a new Act.
     * @return Status code 201 if the post succeeded.
     * @throws RoundException Rerouted to handler.
     * @see ActDTO
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/rounds/{roundId}")
    public ResponseEntity<ActDTO> addAct(@RequestBody @Valid ActDTO actDTO, @PathVariable int roundId,
                                        OAuth2Authentication authentication) throws RoundException {
        roundService.saveAct(roundId, getUserInfo(authentication).get(ID_KEY).toString(),
                actDTO.getType(), actDTO.getPhase(), actDTO.getBet());
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
}
