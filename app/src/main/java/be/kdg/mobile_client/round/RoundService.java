package be.kdg.mobile_client.round;

import be.kdg.mobile_client.room.model.Act;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Service where all api calls to /api/rounds are done.
 */
public interface RoundService {

    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @POST("/api/rounds/act")
    Observable<Response<Void>> addAct(@Body Act act);
}
