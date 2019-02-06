package be.kdg.gameservice.round.service.api;

import be.kdg.gameservice.card.model.Card;
import be.kdg.gameservice.round.model.HandType;

import java.util.List;

public interface HandService {
    HandType determineBestPossibleHand(List<Card> cards);
}
