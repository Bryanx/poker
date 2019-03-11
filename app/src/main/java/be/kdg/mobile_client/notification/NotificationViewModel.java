package be.kdg.mobile_client.notification;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.user.model.Notification;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

public class NotificationViewModel {
    private final NotificationService notificationService;
    private final Context app;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Getter MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();
    @Getter MutableLiveData<String> message = new MutableLiveData<>();

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
    public LiveData<List<Notification>> getNotifications() {
        compositeDisposable.add(notificationService.getNotifications().subscribe(notifications::postValue,
                throwable -> handleError(throwable, app.getString(R.string.load_room_tag), app.getString(R.string.error_loading_rooms))));
        return notifications;
    }

    /**
     * Handles any errors that might occur during the fetching of the data.
     *
     * @param throwable The exception that was thrown.
     * @param tag The tag of the string.
     * @param msg The message that we want to see.
     */
    private void handleError(Throwable throwable, String tag, String msg) {
        if (throwable != null) Log.e(tag, throwable.getMessage());
        message.postValue(msg);
    }
}
