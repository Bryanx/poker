package be.kdg.mobile_client.services;

import be.kdg.mobile_client.model.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Api for sending requests to: https://poker-user-service.herokuapp.com
 */
public interface UserService {
    @Headers({"Content-Type: application/json;charset=UTF-8",
            "Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @POST("/oauth/token")
    Call<Token> login(@Query("username") String username,
                      @Query("password") String password,
                      @Query("grant_type") String grant_type);
}
