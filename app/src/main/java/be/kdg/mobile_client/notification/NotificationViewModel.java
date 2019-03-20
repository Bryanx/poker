package be.kdg.mobile_client.notification;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.R;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

public class NotificationViewModel {
    @Getter private final NotificationService notificationService;
    @Getter MutableLiveData<String> message = new MutableLiveData<>();
    private final MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    private final Context app;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    NotificationViewModel(NotificationService notificationService) {
        this.notificationService = notificationService;
        this.app = App.getContext();
    }

    /**
     * Gives back all the notifications of the user.
     *
     * @return A live data set of the rooms that will be loaded into that list.
     */
    LiveData<List<Notification>> getNotifications() {
        compositeDisposable.add(notificationService.getNotifications().subscribe(notifications::postValue,
                throwable -> handleError(throwable, app.getString(R.string.load_notifications_tag), app.getString(R.string.error_loading_nots))));
        return notifications;
    }

    /**
     * Sends a notification to a specific user.
     *
     * @param receiveId The user that needs to receive the notification.
     * @param notification The notification that needs to be send.
     */
    public void sendNotification(String receiveId, Notification notification) {
        compositeDisposable.add(notificationService.sendNotification(receiveId, notification).subscribe(res -> {},
                throwable -> handleError(throwable, app.getString(R.string.load_notifications_tag), app.getString(R.string.error_loading_nots))));
    }

    /**
     * Deletes all the notifications of a specific user.
     */
    void deleteAllNotifications() {
        compositeDisposable.add(notificationService.deleteNotifications().subscribe());
    }

    /**
     * Handles any errors that might occur during the fetching of the data.
     *
     * @param throwable The exception that was thrown.
     * @param tag       The tag of the string.
     * @param msg       The message that we want to see.
     */
    private void handleError(Throwable throwable, String tag, String msg) {
        if (throwable != null) Log.e(tag, throwable.getMessage());
        message.postValue(msg);
    }
}
