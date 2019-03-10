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
import be.kdg.mobile_client.room.Room;
import be.kdg.mobile_client.room.RoomService;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

public class OverviewViewModel extends ViewModel {
    private final RoomService roomService;
    private final Context app;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Getter MutableLiveData<List<Room>> rooms = new MutableLiveData<>();
    @Getter MutableLiveData<String> message = new MutableLiveData<>();

    @Inject
    OverviewViewModel(RoomService roomService) {
        this.roomService = roomService;
        this.app = App.getContext();
    }

    public LiveData<List<Room>> getPublicRooms() {
        loadData(roomService.getRooms());
        return rooms;
    }

    public LiveData<List<Room>> getPrivateRooms() {
        loadData(roomService.getPrivateRooms());
        return rooms;
    }

    public LiveData<List<Room>> getPrivateRoomsOwner() {
        loadData(roomService.getPrivateRoomsOwner());
        return rooms;
    }

    private void loadData(Observable<List<Room>> roomObs) {
        compositeDisposable.add(roomObs.subscribe(rooms::postValue,
                throwable -> handleError(throwable, app.getString(R.string.load_room_tag), app.getString(R.string.error_loading_rooms))));
    }

    private void handleError(Throwable throwable, String tag, String msg) {
        if (throwable != null) Log.e(tag, throwable.getMessage());
        message.postValue(msg);
    }
}
