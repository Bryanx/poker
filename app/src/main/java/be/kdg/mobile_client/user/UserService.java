package be.kdg.mobile_client.user;

import java.util.List;

import be.kdg.mobile_client.user.authorization.Register;
import be.kdg.mobile_client.user.authorization.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Api for sending requests to: https://poker-user-service.herokuapp.com
 * An authentication token is send on each call.
 */
public interface UserService {
    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0",
            "No-Authentication: true"})
    @POST("/oauth/token")
    Call<Token> login(@Query("username") String username,
                      @Query("password") String password,
                      @Query("grant_type") String grant_type);

    @Headers({"No-Authentication: true"})
    @POST("/api/user")
    Call<Token> register(@Body Register register);

    @GET("/api/user/{id}")
    Call<User> getUser(@Path("id") String id);

    @GET("/api/users/{name}")
    Call<List<User>> getUsersByName(@Path("name") String name);

    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @PUT("/api/user")
    Call<Token> changeUser(@Body User user);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @PATCH("/api/user")
    Call<Token> changePassword(@Body Register authDTO);
}
