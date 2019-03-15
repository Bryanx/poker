package be.kdg.gameservice.replay.service.impl;

import be.kdg.gameservice.replay.model.Replay;
import be.kdg.gameservice.replay.persistence.ReplayRepository;
import be.kdg.gameservice.replay.service.api.ReplayService;
import be.kdg.gameservice.room.model.Room;
import be.kdg.gameservice.round.model.Act;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.shared.UserApiGateway;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

/**
 * This service will be used to generate replays for the played rounds.
 * This service will also manage the CRUD of replays.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ReplayServiceImpl implements ReplayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayServiceImpl.class);
    private final UserApiGateway userApiGateway;
    private final ReplayRepository replayRepository;

    /**
     * Creates a replay for every player in that round of the room.
     *
     * @param room The room that the replay needs to be created for.
     */
    public void createReplays(Room room) {
        Round round = room.getCurrentRound();
        round.getPlayersInRound().forEach(player -> {
            createReplay(player.getUserId(), room.getName(), round.getId(), round.getActs());
        });
    }

    /**
     * Creates a single replay for player.
     *
     * @param ownerId     The owner id of the replay
     * @param roomName    The name of the room.
     * @param roundNumber The round number of the current round.
     * @param acts        All the acts that were played in that round.
     */
    private void createReplay(String ownerId, String roomName, int roundNumber, List<Act> acts) {
        //Make replay
        Replay replay = new Replay(roomName, ownerId, roundNumber);

        //Construct replay
        String userName =  userApiGateway.getUser(acts.get(0).getPlayer().getUserId()).getUsername();
        acts.stream().sorted(Act::compareTo)
                .forEach(act -> {
                    String line = String.format("%s played act %s for %d chips",
                            userName, act.getType(), act.getBet());
                    replay.addLine(line, act.getPhase().toString());
                });

        //Save replay
        LOGGER.info("Generate replay for " + ownerId + " that is based on round " + roundNumber);
        replayRepository.save(replay);
    }

    /**
     * Gives back all the replays from a specific user.
     *
     * @param ownerId The id of the owner of the replays.
     * @return All the replays that are from the owner.
     */
    @Override
    public List<Replay> getReplays(String ownerId) {
        return Collections.unmodifiableList(replayRepository.getAllByOwnerId(ownerId));
    }
}
