package be.kdg.mobile_client.room.model;

import java.util.Objects;

import lombok.Data;

@Data
public class Player {
    private int id;
    private String userId;
    private String username;
    private Card firstCard;
    private Card secondCard;
    private ActType lastAct;
    private boolean inRound;
    private boolean inRoom;
    private int chipCount;
    private String handType;
    private int seatNumber;
    private String access_token;

    private transient boolean myTurn;
    private transient String bet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(userId, player.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
