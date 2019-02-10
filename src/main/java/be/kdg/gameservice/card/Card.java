package be.kdg.gameservice.card;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Wrapper class used to store information about a card.
 */
@Entity
@Table(name = "card")
@NoArgsConstructor
public final class Card {
    /**
     * The id of the card. Used for persistence.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     * The type of card.
     *
     * @see CardType
     */
    @Getter
    public CardType type;

    public Card(CardType type) {
        this.type = type;
    }
}
