package be.kdg.mobile_client.round;

import java.util.List;

import be.kdg.mobile_client.room.model.Act;
import be.kdg.mobile_client.room.model.ActType;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Service where all api calls to /api/rounds are done.
 */
public interface RoundService {

    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @POST("/api/rounds/act")
    Observable<Response<Void>> addAct(@Body Act act);

    @GET("/api/rounds/{roundId}/possible-acts")
    Observable<List<ActType>> getPossibleActs(@Path("roundId") String roundId);

}
