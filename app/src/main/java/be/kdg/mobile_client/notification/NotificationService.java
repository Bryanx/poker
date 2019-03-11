package be.kdg.mobile_client.notification;

import java.util.List;

import be.kdg.mobile_client.user.model.Notification;
import be.kdg.mobile_client.user.model.User;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Api for sending requests to: https://poker-user-service.herokuapp.com
 * An authentication token is send on each call.
 */
public interface NotificationService {
    @GET("/api/user/notifications")
    Observable<List<Notification>> getNotifications();
}
