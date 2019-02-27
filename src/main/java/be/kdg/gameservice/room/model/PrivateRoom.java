package be.kdg.gameservice.room.model;

import lombok.Getter;
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
    @Getter
    private String ownerId;

    public PrivateRoom(GameRules gameRules, String name, String ownerId) {
        super(gameRules, name);
        this.ownerId = ownerId;
        this.whiteListedPlayers = new ArrayList<>();
        this.whiteListedPlayers.add(new WhiteListedUser(ownerId));
    }

    public PrivateRoom(String name, String ownerUserId) {
        this(new GameRules(), name, ownerUserId);
    }

    public void addWhiteListedPlayer(WhiteListedUser user) {
        this.whiteListedPlayers.add(user);
    }

    public void deleteWhiteListedPlayer(WhiteListedUser user) {
        this.whiteListedPlayers.remove(user);
    }

    public List<WhiteListedUser> getWhiteListedPlayers() {
        return Collections.unmodifiableList(whiteListedPlayers);
    }


}
