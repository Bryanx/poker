package be.kdg.mobile_client.user;

import java.util.List;

import be.kdg.mobile_client.user.authorization.Credential;
import be.kdg.mobile_client.user.authorization.Token;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Api for sending requests to: https://poker-user-service.herokuapp.com
 * An authentication token is send on each call.
 */
public interface UserService {

    @GET("/api/user/{id}")
    Observable<User> getUser(@Path("id") String id);

    @GET("/api/users/{name}")
    Observable<List<User>> getUsersByName(@Path("name") String name);

    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @PUT("/api/user")
    Observable<Token> changeUser(@Body User user);

    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @PATCH("/api/user")
    Observable<Token> changePassword(@Body Credential authDTO);
}
