package be.kdg.mobile_client.room.viewmodel;

import android.content.Context;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.room.model.Room;
import be.kdg.mobile_client.room.RoomService;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

/**
 * This view model is used for communication with the room service back end.
 */
public class OverviewViewModel extends ViewModel {
    private final RoomService roomService;
    private final Context app;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Getter MutableLiveData<List<Room>> rooms = new MutableLiveData<>();
    @Getter MutableLiveData<String> message = new MutableLiveData<>();

    @Inject
    public OverviewViewModel(RoomService roomService) {
        this.roomService = roomService;
        this.app = App.getContext();
    }

    /**
     * Gives back all the public rooms.
     *
     * @return A live data set of the rooms that will be loaded into that list.
     */
    public LiveData<List<Room>> getPublicRooms() {
        loadData(roomService.getRooms());
        return rooms;
    }

    /**
     * Gives back all the private rooms.
     *
     * @return A live data set of the rooms that will be loaded into that list.
     */
    public LiveData<List<Room>> getPrivateRooms() {
        loadData(roomService.getPrivateRooms());
        return rooms;
    }

    /**
     * Gives back all the private rooms that the current user has ownership over.
     *
     * @return A live data set of the rooms that will be loaded into that list.
     */
    public LiveData<List<Room>> getPrivateRoomsOwner() {
        loadData(roomService.getPrivateRoomsOwner());
        return rooms;
    }

    /**
     * Loads the data into the live data sets.
     *
     * @param roomObs The observable that the actions need to happen on.
     */
    private void loadData(Observable<List<Room>> roomObs) {
        compositeDisposable.add(roomObs.subscribe(rooms::postValue,
                throwable -> handleError(throwable, app.getString(R.string.load_room_tag), app.getString(R.string.error_loading_rooms))));
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
