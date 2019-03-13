package be.kdg.userservice.notification.model;

/**
 * All the types of notifications that a user can receive.
 */
public enum NotificationType {
    FRIEND_REQUEST("Friend request"),
    ADD_PRIVATE_ROOM("Invitation for room!"),
    DELETE_PRIVATE_ROOM("Removal from room..."),
    GAME_REQUEST("Game request"),
    GLOBAL_MESSAGE("Announcement");

    private String title;

    NotificationType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
