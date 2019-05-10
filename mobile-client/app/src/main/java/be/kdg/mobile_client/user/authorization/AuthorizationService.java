package be.kdg.mobile_client.user.authorization;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Api for sending authorization requests
 */
public interface AuthorizationService {

    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0",
            "No-Authentication: true"})
    @POST("/oauth/token")
    Observable<Token> login(@Query("username") String username,
                            @Query("password") String password,
                            @Query("grant_type") String grant_type);

    @Headers({"No-Authentication: true"})
    @POST("/api/user")
    Observable<Token> register(@Body Credential credential);
}
