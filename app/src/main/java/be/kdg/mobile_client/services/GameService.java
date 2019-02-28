package be.kdg.mobile_client.services;

import java.util.List;

import be.kdg.mobile_client.model.Player;
import be.kdg.mobile_client.model.Room;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * This service will be used to send API requests to https://poker-game-service.herokuapp.com
 * game micro service back end.
 */
public interface GameService {
    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/rooms")
    Call<List<Room>> getRooms();

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/rooms/{id}")
    Call<Room> getRoom(@Path("id") int id);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/rooms/{id}/join")
    Call<Player> joinRoom(@Path("id") int id);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/rooms/{id}/current-round")
    Call<Void> getCurrentRound(@Path("id") int id);
}
