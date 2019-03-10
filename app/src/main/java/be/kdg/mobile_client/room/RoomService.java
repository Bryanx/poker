package be.kdg.mobile_client.room;

import java.util.List;

import be.kdg.mobile_client.room.model.Player;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * This service will be used to send API requests to https://poker-game-service.herokuapp.com
 * game micro service back end.
 */
public interface RoomService {
    @GET("/api/rooms")
    Observable<List<Room>> getRooms();

    @GET("/api/rooms/private")
    Observable<List<Room>> getPrivateRooms();

    @GET("/api/rooms/private/owner")
    Observable<List<Room>> getPrivateRoomsOwner();

    @GET("/api/rooms/{id}")
    Observable<Room> getRoom(@Path("id") int id);

    @GET("/api/rooms/{id}/join")
    Observable<Player> joinRoom(@Path("id") int id);

    @DELETE("/api/rooms/{id}/leave-room")
    Observable<Response<Void>> leaveRoom(@Path("id") int id);

    @GET("/api/rooms/{id}/current-round")
    Observable<Response<Void>> getCurrentRound(@Path("id") int id);
}
