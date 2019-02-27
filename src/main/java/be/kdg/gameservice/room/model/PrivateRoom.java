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
public final class PrivateRoom extends Room {
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<WhiteListedUser> whiteListedPlayers;
    private String ownerUserId;

    public PrivateRoom(GameRules gameRules, String name, String ownerUserId) {
        super(gameRules, name);
        this.ownerUserId = ownerUserId;
        this.whiteListedPlayers = new ArrayList<>();
        this.whiteListedPlayers.add(new WhiteListedUser(ownerUserId));
    }

    public PrivateRoom(String name, String ownerUserId) {
        this(new GameRules(), name, ownerUserId);
    }

    public void addWhiteListedPlayer(WhiteListedUser player) {
        this.whiteListedPlayers.add(player);
    }

    public void deleteWhiteListedPlayer(WhiteListedUser player) {
        this.whiteListedPlayers.remove(player);
    }

    public List<WhiteListedUser> getWhiteListedPlayers() {
        return Collections.unmodifiableList(whiteListedPlayers);
    }


}
