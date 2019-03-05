package be.kdg.mobile_client.services;

import be.kdg.mobile_client.model.Act;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RoundService {

    @Headers({"Authorization: Basic bXktdHJ1c3RlZC1jbGllbnQ6c2VjcmV0"})
    @POST("/api/rounds/{id}/act")
    Observable<Response<Void>> addAct(@Body Act act);
}
