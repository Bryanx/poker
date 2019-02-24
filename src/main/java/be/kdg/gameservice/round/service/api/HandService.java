package be.kdg.gameservice.round.service.api;


import be.kdg.gameservice.card.Card;
import be.kdg.gameservice.round.model.Hand;

import java.util.List;

public interface HandService {
    Hand determineBestPossibleHand(List<Card> cards);
}
