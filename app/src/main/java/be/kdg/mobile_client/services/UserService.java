package be.kdg.mobile_client.services;

import be.kdg.mobile_client.model.Register;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Api for sending requests to: https://poker-user-service.herokuapp.com
 * An authentication token is send on each call.
 */
public interface UserService {
    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0",
            "No-Authentication: true"})
    @POST("/oauth/token")
    Call<Token> login(@Query("username") String username,
                      @Query("password") String password,
                      @Query("grant_type") String grant_type);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8",
            "No-Authentication: true"})
    @POST("/api/user")
    Call<Token> register(@Body Register register);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/user")
    Call<User> getMySelf();

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/user/{id}")
    Call<User> getUser(@Path("id") String id);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/users")
    Call<User[]> getUsers();

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Accept: application/json; charset=utf-8"})
    @GET("/api/users/{name}")
    Call<User[]> getUserByName(@Path("name") String name);
}
