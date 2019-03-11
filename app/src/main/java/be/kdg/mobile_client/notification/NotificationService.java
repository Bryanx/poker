package be.kdg.mobile_client.notification;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Api for sending requests to: https://poker-user-service.herokuapp.com
 * An authentication token is send on each call.
 */
public interface NotificationService {
    @GET("/api/user/notifications")
    Observable<List<Notification>> getNotifications();

    @DELETE("/api/user/notification/{id}")
    Observable<Void> deleteNotification(@Path("id") int id);

    @DELETE("/api/user/notification")
    Observable<Void> deleteNotifications();
}
