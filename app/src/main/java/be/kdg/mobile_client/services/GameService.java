package be.kdg.mobile_client.services;

import be.kdg.mobile_client.model.Room;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * This service will be used to send API requests to the
 * game micro service back end.
 */
public interface GameService {
    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/roomgs")
    Call<Room[]> getRooms();
}
