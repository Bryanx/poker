package be.kdg.gameservice.round.controller;

import be.kdg.gameservice.round.controller.dto.ActDTO;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.service.api.RoundService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RoundApiController {
    private final ModelMapper modelMapper;
    private final RoundService roundService;

    @Autowired
    public RoundApiController(ModelMapper modelMapper, RoundService roundService) {
        this.modelMapper = modelMapper;
        this.roundService = roundService;
    }

    @GetMapping("/rounds/[roundId]/players/[playerId}/possible-acts")
    public ResponseEntity<ActType[]> getPossibleActs(@PathVariable int roundId, @PathVariable int playerId) throws RoundException {
        List<ActType> actTypes = roundService.getPossibleActs(roundId, playerId);
        return new ResponseEntity<>(modelMapper.map(actTypes, ActType[].class), HttpStatus.OK);
    }

    @PostMapping("/rounds/acts")
    public ResponseEntity<ActDTO> saveAct(@RequestBody @Valid ActDTO actDTO) throws RoundException {
        roundService.addAct(actDTO.getRoundId(), actDTO.getPlayerId(),
                actDTO.getType(), actDTO.getPhase(), actDTO.getBet());
        return new ResponseEntity<>(actDTO, HttpStatus.CREATED);
    }
}
