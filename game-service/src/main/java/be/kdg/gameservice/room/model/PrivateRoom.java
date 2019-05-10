package be.kdg.gameservice.room.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class will be used to store information about a private room0
 * A private room is a room that is managed by a specific player.
 */
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "PRIVATE")
public final class PrivateRoom extends Room {
    /**
     * Whitelisted users are users that have the privilege to join this
     * private room.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    @Fetch(value = FetchMode.SUBSELECT)
    private List<WhiteListedUser> whiteListedUsers;

    /**
     * The user Id of the owner of this private room.
     */
    @Getter
    private String ownerId;

    /**
     * @param gameRules The game rules that apply for this private room.
     * @param name      The name of the private room.
     * @param ownerId   The owner of this private room.
     */
    public PrivateRoom(GameRules gameRules, String name, String ownerId) {
        super(gameRules, name);
        this.ownerId = ownerId;
        this.whiteListedUsers = new ArrayList<>();
        this.whiteListedUsers.add(new WhiteListedUser(ownerId));
    }

    /**
     * @param name    The name of the private room.
     * @param ownerId The owner of this private room.
     */
    public PrivateRoom(String name, String ownerId) {
        this(new GameRules(), name, ownerId);
    }

    /**
     * Adds a user to the white list.
     *
     * @param user The user that needs to be added.
     */
    public void addWhiteListedPlayer(WhiteListedUser user) {
        this.whiteListedUsers.add(user);
    }

    /**
     * Removes a user to the white list.
     *
     * @param user The user that needs to be removed.
     */
    public void deleteWhiteListedPlayer(WhiteListedUser user) {
        this.whiteListedUsers.remove(user);
    }

    /**
     * @return An unmodifiable collection of all whitelisted users.
     */
    public List<WhiteListedUser> getWhiteListedUsers() {
        return Collections.unmodifiableList(whiteListedUsers);
    }
}
