package be.kdg.gameservice.round.service.impl;

import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.*;
import be.kdg.gameservice.round.persistence.RoundRepository;
import be.kdg.gameservice.round.service.api.HandService;
import be.kdg.gameservice.round.service.api.RoundService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * This service will be used to manage the ongoing activity of a specific round.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RoundServiceImpl implements RoundService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoundServiceImpl.class);
    private final RoundRepository roundRepository;
    private final HandService handService;

    /**
     * This method will check if the act done by the player is actually possible.
     * If that is the case, the act will be created and persisted.
     *
     * @param roundId  The id used to get player from database.
     * @param userId The id used to get player from database.
     * @param type     The type of act.
     * @param bet      The bet associated with the act.
     * @throws RoundException Thrown if the act is invalid or if the player is not present.
     * @see Act The get insight in the constructor.
     * @see ActType To get insight in the types of acts that are possbile.
     * @see Round To get insight in all the util methods that are used.
     * @see Player To get insight in all the util methods that are used.
     */
    @Override
    public void saveAct(int roundId, String userId, ActType type, Phase phase, int bet, boolean allIn) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        Optional<Player> playerOpt = round.getPlayersInRound().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny();

        //Check if player exists
        if (!playerOpt.isPresent())
            throw new RoundException(RoundServiceImpl.class, "playerId could not be associated with round.");

        //Do act & add act to round
        Player player = playerOpt.get();
        if (checkIfActPossible(round, player, type, bet)) {
            LOGGER.info(String.format("Added act (%s) from %d to round %d", type, player.getId(), round.getId()));
            round.addAct(new Act(player, type, phase, bet));
            round.setPot(round.getPot() + bet);
            player.setLastAct(type);
            player.setChipCount(player.getChipCount() - bet);
            player.setAllIn(allIn);
            checkEndOfPhase(round);
            //update database
            saveRound(round);
        } else throw new RoundException(RoundServiceImpl.class, "The act was not possible to make.");
    }

    /**
     * Check if the current Phase of round is finished
     * @param round
     */
    private void checkEndOfPhase(Round round) {
        Phase currentPhase = round.getCurrentPhase();
        int checkCount = round.getActs().stream()
                .filter(a -> a.getPhase() == currentPhase)
                .filter(a -> a.getType() == ActType.CHECK || a.getType() == ActType.FOLD)
                .toArray()
                .length;
        // Check if enough Checks of Folds are made
        if (checkCount == round.getActivePlayers().size()) {
            round.nextPhase();
        } else {
            checkEndOfPhaseWithBetOrRaise(round);
        }
    }

    /**
     * Check if phase has ended when enough Call/Folds are followed by a Bet of Raise
     * @param round
     * @return
     */
    private void checkEndOfPhaseWithBetOrRaise(Round round) {
        Phase currentPhase = round.getCurrentPhase();
        int lastAct = -1;
        for (Act act: round.getActs()) {
            if(act.getPhase() == currentPhase) {
                if(act.getType() == ActType.BET || act.getType() == ActType.RAISE) {
                    lastAct = round.getActs().indexOf(act);
                }
            }
        }
        for (int i = 0; i < round.getActs().size(); i++) {
            if(round.getActs().get(i).getPhase() == currentPhase) {
                if(round.getActs().get(i).getType() == ActType.BET || round.getActs().get(i).getType() == ActType.RAISE) {
                    lastAct = i;
                }
            }
        }

        if(lastAct != -1) {
            List<Act> lastActs = round.getActs().subList(lastAct, round.getActs().size());

            if(lastActs.stream().filter(a -> a.getType() == ActType.CALL).toArray().length == round.getActivePlayers().size() - 1) {
                round.nextPhase();
            }
        }
    }

    @Override
    public Optional<Player> checkEndOfRound(int roundId) throws RoundException{
        Round round = getRound(roundId);
        Phase currentPhase = round.getCurrentPhase();

        if (currentPhase == Phase.SHOWDOWN) {
            Player winningPlayer = determineWinner(roundId);
            return Optional.of(distributeCoins(roundId, winningPlayer));
        }
        return Optional.empty();
    }

    /**
     * Associates the played act with a case in the switch. If that check is valid,
     * then the act is possible. If the player has insufficient chips, then the method
     * will immediately return false.
     *
     * @param round  The round the check needs to happen on.
     * @param player The player who did the act.
     * @param type   The type of act the player did.
     * @param bet    The bet the player wants to make.
     * @return True if the act is valid.
     * @throws RoundException Thrown if the act is invalid, or if the player has insufficient chips.
     * @see ActType To get insight in the types of acts that are possbile.
     * @see Round To get insight in all the util methods that are used.
     * @see Player To get insight in all the util methods that are used.
     */
    private boolean checkIfActPossible(Round round, Player player, ActType type, int bet) throws RoundException {
        //Check chips
        if (player.getChipCount() < bet)
            throw new RoundException(RoundServiceImpl.class, "The bet is higher than the chip count of the player.");

        //Check act type
        switch (type) {
            case UNDECIDED:
                throw new RoundException(RoundServiceImpl.class, "This act should not be possible.");
            case FOLD:
                return true;
            case BET:
                return checkBet(round);
            case CALL:
                return checkCall(round.getOtherPlayers(player));
            case CHECK:
                return checkCheck(round, round.getOtherPlayers(player));
            case RAISE:
                return checkRaise(round);
            default:
                throw new RoundException(RoundServiceImpl.class, "Act was not given.");
        }
    }

    /**
     * Returns all the possible acts that can be done by a specific player. Acts RAISE and FOLD
     * will automatically be added, because those are always possible.
     *
     * @param roundId  The id used to get player from database.
     * @param userId The id used to get player from database.
     * @return An unmodifiable list of all the possible actions.
     * @throws RoundException Thrown if the player does not exists.
     * @see ActType To get insight in the types of acts that are possbile.
     * @see Round To get insight in all the util methods that are used.
     * @see Player To get insight in all the util methods that are used.
     */
    @Override
    public List<ActType> getPossibleActs(int roundId, String userId) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        Optional<Player> playerOpt = round.getPlayersInRound().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny();

        //Check if player exists
        if (!playerOpt.isPresent())
            throw new RoundException(RoundServiceImpl.class, "playerId could not be associated with round.");

        //Get possible acts for player.
        Player player = playerOpt.get();
        List<ActType> types = new ArrayList<>(Collections.singletonList(ActType.FOLD));

        if (checkBet(round)) types.add(ActType.BET);
        if (checkCall(round.getOtherPlayers(player))) types.add(ActType.CALL);
        if (checkCheck(round, round.getOtherPlayers(player))) types.add(ActType.CHECK);
        if (checkRaise(round)) types.add(ActType.RAISE);

        return Collections.unmodifiableList(types);
    }

    /**
     * @param participatingPlayers The players that need to participate in this round.
     * @param button The button, needs to be moved to the next player.
     * @return The newly created round.
     * @see Round to get insight in the constructor
     */
    @Override
    public Round startNewRound(List<Player> participatingPlayers, int button) {
        button = button >= participatingPlayers.size() - 1 ? 0 : button + 1;
        participatingPlayers.forEach(Player::resetPlayer);
        return new Round(participatingPlayers, button);
    }

    /**
     * Checks if the CALL-act is possible at this point in the round.
     * You can CALL if one of the other players has BET or RAISED.
     *
     * @param others All the other players in the round.
     * @return True if a CALL is possible.
     * @see ActType To get insight in the types of acts that are possbile.
     */
    private boolean checkCall(List<Player> others) {
        Optional<Player> playerOpt = others.stream()
                .filter(p -> p.getLastAct().equals(ActType.RAISE) ||
                        p.getLastAct().equals(ActType.BET))
                .findAny();

        return playerOpt.isPresent();
    }

    /**
     * Checks if the CHECK-act is possible at this point in the round.
     * You can CHECK if other players didn't BET, RAISE or CALL.
     *
     * @param round
     * @param others All the other players in the round.
     * @return True if a CHECK is possible.
     * @see ActType To get insight in the types of acts that are possbile.
     */
    private boolean checkCheck(Round round, List<Player> others) {
        int lastBetRaiseAct = -1;
        for (int i = 0; i < round.getActs().size(); i++) {
            if(round.getActs().get(i).getPhase() == round.getCurrentPhase()) {
                if(round.getActs().get(i).getType() == ActType.BET || round.getActs().get(i).getType() == ActType.RAISE) {
                    lastBetRaiseAct = i;
                }
            }
        }

        if(lastBetRaiseAct == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if the BET-act is possible at this point in the round.
     * You can BET if nothing has gone into the pot this round.
     *
     * @param round The round of the act
     * @return True if a BET is possible.
     * @see ActType To get insight in the types of acts that are possbile.
     */
    private boolean checkBet(Round round) {
        Optional<Act> actOpt = round.getActs().stream()
                .filter(a -> a.getPhase().equals(round.getCurrentPhase()))
                .filter(a -> a.getType().equals(ActType.BET))
                .findAny();

        return !actOpt.isPresent();
    }

    /**
     * Checks if the RAISE-act is possible at this point in the round.
     *
     * @param round All the other players in the round.
     * @return True if a RAISE is possible.
     * @see ActType To get insight in the types of acts that are possbile.
     */
    private boolean checkRaise(Round round) {
        Optional<Act> actOpt = round.getActs().stream()
                .filter(a -> a.getPhase().equals(round.getCurrentPhase()))
                .filter(a -> a.getType().equals(ActType.BET))
                .findAny();

        return actOpt.isPresent();
    }

    /**
     * @param roundId The id used to retrieve the round.
     * @return The round that corresponds with the id.
     * @throws RoundException If the round was not found in the database.
     */
    private Round getRound(int roundId) throws RoundException {
        return roundRepository.findById(roundId)
                .orElseThrow(() -> new RoundException(RoundServiceImpl.class, "The round was not found in the database."));
    }

    /**
     * Saves or update a round in the database.
     *
     * @param round The round that needs to be updated or saved.
     * @return The updated or saved round
     * @see Round
     */
    private Round saveRound(Round round) {
        return roundRepository.save(round);
    }

    /**
     * Distributes the pot to the winner and resets the pot
     *
     * @param roundId
     * @param player
     */
    @Override
    public Player distributeCoins(int roundId, Player player) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        Player winningPlayer = round.getPlayersInRound().stream().filter(player1 ->
                player1.getUserId().equals(player.getUserId())).findFirst().orElseThrow(()
                -> new RoundException(RoundServiceImpl.class, "The winning player was not found in the round."));
        winningPlayer.setChipCount(player.getChipCount() + round.getPot());
        roundRepository.save(round);
        return winningPlayer;
    }

    /**
     * Determines winning player based on all hand combinations of all the players
     *
     * @param roundId
     * @return
     * @throws RoundException
     */
    public Player determineWinner(int roundId) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        List<Player> participatingPlayers = round.getPlayersInRound()
                .stream()
                .filter(player -> player.getLastAct() != ActType.FOLD)
                .collect(toList());

        Hand bestHand = null;
        Player winningPlayer = null;

        for (Player player: participatingPlayers) {
            Hand playerHand = this.bestHandForPlayer(player, round);
            player.setHandType(playerHand.getHandType());
            if(bestHand == null) {
                winningPlayer = player;
                bestHand = playerHand;
            } else if(playerHand.compareTo(bestHand) > 0) {
                winningPlayer = player;
                bestHand = playerHand;
            }
        }
        saveRound(round);
        return winningPlayer;
    }

    /**
     * Returns best Hand based on all possibilities out of 7 cards for player.
     * @param player
     * @param round
     * @return
     */
    private Hand bestHandForPlayer(Player player, Round round) {
        // Array of 7  cards -> 5 (boardCards) + 1 (player FirstCard) + 1 (player SecondCard)
        List<Card> playerCards = new ArrayList<>(round.getCards());
        playerCards.addAll(Arrays.asList(player.getFirstCard(), player.getSecondCard()));

        return handService.determineBestPossibleHand(playerCards);
    }

    @Override
    public List<Round> getRounds() {
        return Collections.unmodifiableList(roundRepository.findAll());
    }

    /**
     * Checks which player should play the next act.
     */
    @Override
    public String determineNextUserId(int roundId, String userId) throws RoundException{
        Round round = getRound(roundId);
        Optional<Player> playerOpt = round.getPlayersInRound().stream()
                .filter(p -> p.getUserId().equals(userId))
                .findAny();

        if (!playerOpt.isPresent())
            throw new RoundException(RoundServiceImpl.class, "playerId could not be associated with round.");

        Player player = playerOpt.get();

        List<Player> sortedActivePlayers = round.getActivePlayers().stream()
                .sorted(Comparator.comparingInt(Player::getSeatNumber))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));

        for (Player activePlayer : sortedActivePlayers) {
            if (activePlayer.getSeatNumber() > player.getSeatNumber()) {
                return activePlayer.getUserId();
            }
        }

        Optional<Player> optionalPlayer = round.getActivePlayers().stream().min(Comparator.comparing(Player::getSeatNumber));
        if (optionalPlayer.isPresent()) {
            return optionalPlayer.get().getUserId();
        } else {
            throw new RoundException(RoundServiceImpl.class, "No suitable players found.");
        }
    }
}
