package be.kdg.gameservice.room.model;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "PRIVATE")
public class PrivateRoom extends Room {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<WhiteListedPlayer> whiteListedPlayers;

    public PrivateRoom(GameRules gameRules, String name) {
        super(gameRules, name);
        this.whiteListedPlayers = new ArrayList<>();
    }

    public void addWhiteListedPlayer(WhiteListedPlayer player) {
        this.whiteListedPlayers.add(player);
    }

    public List<WhiteListedPlayer> getWhiteListedPlayers() {
        return Collections.unmodifiableList(whiteListedPlayers);
    }
}
