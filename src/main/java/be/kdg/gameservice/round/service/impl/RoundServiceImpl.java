package be.kdg.gameservice.round.service.impl;

import be.kdg.gameservice.card.model.Card;
import be.kdg.gameservice.room.model.Player;
import be.kdg.gameservice.round.Hand;
import be.kdg.gameservice.round.exception.RoundException;
import be.kdg.gameservice.round.model.Act;
import be.kdg.gameservice.round.model.ActType;
import be.kdg.gameservice.round.model.Phase;
import be.kdg.gameservice.round.model.Round;
import be.kdg.gameservice.round.persistence.PlayerRepository;
import be.kdg.gameservice.round.persistence.RoundRepository;
import be.kdg.gameservice.round.service.api.RoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * This service will be used to manage the ongoing activity of a specific round.
 * It will also take care of the CRUD operations with its persistence dependency.
 */
@Service
@Transactional
public class RoundServiceImpl implements RoundService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoundServiceImpl.class);
    private final RoundRepository roundRepository;
    private final PlayerRepository playerRepository;

    @Autowired
    public RoundServiceImpl(RoundRepository roundRepository, PlayerRepository playerRepository) {
        this.roundRepository = roundRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * This method will check if the act done by the player is actually possible.
     * If that is the case, the act will be created and persisted.
     *
     * @param roundId  The id used to get player from database.
     * @param playerId The id used to get player from database.
     * @param type     The type of act.
     * @param bet      The bet associated with the act.
     * @throws RoundException Thrown if the act is invalid or if the player is not present.
     * @see Act The get insight in the constructor.
     * @see ActType To get insight in the types of acts that are possbile.
     * @see Round To get insight in all the util methods that are used.
     * @see Player To get insight in all the util methods that are used.
     */
    @Override
    public void addAct(int roundId, int playerId, ActType type, Phase phase, int bet) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        Optional<Player> playerOpt = round.getParticipatingPlayers().stream()
                .filter(p -> p.getId() == playerId)
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

            //update database
            saveRound(round);
        } else throw new RoundException(RoundServiceImpl.class, "The act was not possible to make.");
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
                return checkCheck(round.getOtherPlayers(player));
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
     * @param playerId The id used to get player from database.
     * @return An unmodifiable list of all the possible actions.
     * @throws RoundException Thrown if the player does not exists.
     * @see ActType To get insight in the types of acts that are possbile.
     * @see Round To get insight in all the util methods that are used.
     * @see Player To get insight in all the util methods that are used.
     */
    @Override
    public List<ActType> getPossibleActs(int roundId, int playerId) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        Optional<Player> playerOpt = round.getParticipatingPlayers().stream()
                .filter(p -> p.getId() == playerId)
                .findAny();

        //Check if player exists
        if (!playerOpt.isPresent())
            throw new RoundException(RoundServiceImpl.class, "playerId could not be associated with round.");

        //Get possible acts for player.
        Player player = playerOpt.get();
        List<ActType> types = new ArrayList<>(Collections.singletonList(ActType.FOLD));

        if (checkBet(round)) types.add(ActType.BET);
        if (checkCall(round.getOtherPlayers(player))) types.add(ActType.CALL);
        if (checkCheck(round.getOtherPlayers(player))) types.add(ActType.CHECK);
        if (checkRaise(round)) types.add(ActType.RAISE);

        return Collections.unmodifiableList(types);
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
     * @param others All the other players in the round.
     * @return True if a CHECK is possible.
     * @see ActType To get insight in the types of acts that are possbile.
     */
    private boolean checkCheck(List<Player> others) {
        Optional<Player> playerOpt = others.stream()
                .filter(p -> p.getLastAct().equals(ActType.RAISE) ||
                        p.getLastAct().equals(ActType.CALL) ||
                        p.getLastAct().equals(ActType.BET))
                .findAny();

        return !playerOpt.isPresent();
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
    @Override
    public Round getRound(int roundId) throws RoundException {
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
    @Override
    public Round saveRound(Round round) {
        return roundRepository.save(round);
    }

    @Override
    public Round startNewRound() {
        //TODO: implement.
        return null;
    }

    /**
     * Determines winning player based on all hand combinations of all the players
     * @param roundId
     * @return
     * @throws RoundException
     */
    public Player determineWinningPlayer(int roundId) throws RoundException {
        //Get data
        Round round = getRound(roundId);
        List<Player> participatingPlayers = round.getParticipatingPlayers();

        Hand bestHand = null;
        Player winningPlayer = null;
        for (Player player: participatingPlayers) {
            Hand bestHandForPlayer = this.bestHandForPlayer(player, round);

            if(bestHandForPlayer.compareTo(bestHand) > 0) {
                winningPlayer = player;
                bestHand = bestHandForPlayer;
            }
        }
        return winningPlayer;
    }

    /**
     * Returns best hand based on all possibilities based for player.
     * @param player
     * @param round
     * @return
     */
    private Hand bestHandForPlayer(Player player, Round round) {
        // Array of 7  cards -> 5 (boardCards) + 1 (player FirstCard) + 1 (player SecondCard)
        List<Card> playerCards = new ArrayList<>(round.getCards());
        playerCards.addAll(Arrays.asList(player.getFirstCard(), player.getSecondCard()));

        List<Set<Card>> res = new ArrayList<>();
        getSubsets(playerCards, 5, 0, new HashSet<Card>(), res);

        List<Hand> allHands = new ArrayList<>();
        for (Set<Card> handPossibility : res) {
            Hand hand = this.createHand(new ArrayList<>(handPossibility));
            allHands.add(hand);
        }

        Collections.sort(allHands);
        return allHands.get(allHands.size() - 1);
    }

    /**
     * Construct new Hand that calculates handType
     * @param cards
     * @return
     */
    private Hand createHand(List<Card> cards) {
        return new Hand(cards);
    }

    /**
     * Creates all subsets of size k based on superSet
     * @param superSet
     * @param k
     * @param idx
     * @param current
     * @param solution
     */
    private void getSubsets(List<Card> superSet, int k, int idx, Set<Card> current, List<Set<Card>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Card x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }
}
