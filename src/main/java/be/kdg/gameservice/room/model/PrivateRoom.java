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
    private List<WhiteListedUser> whiteListedUsers;
    @Getter
    private String ownerId;

    public PrivateRoom(GameRules gameRules, String name, String ownerId) {
        super(gameRules, name);
        this.ownerId = ownerId;
        this.whiteListedUsers = new ArrayList<>();
        this.whiteListedUsers.add(new WhiteListedUser(ownerId));
    }

    public PrivateRoom(String name, String ownerUserId) {
        this(new GameRules(), name, ownerUserId);
    }

    public void addWhiteListedPlayer(WhiteListedUser user) {
        this.whiteListedUsers.add(user);
    }

    public void deleteWhiteListedPlayer(WhiteListedUser user) {
        this.whiteListedUsers.remove(user);
    }

    public List<WhiteListedUser> getWhiteListedUsers() {
        return Collections.unmodifiableList(whiteListedUsers);
    }


}
