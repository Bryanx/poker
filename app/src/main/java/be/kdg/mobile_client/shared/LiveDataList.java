package be.kdg.mobile_client.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import androidx.lifecycle.MutableLiveData;

/**
 * Simple wrapper class for livedata with lists.
 * @param <T> datatype
 */
public class LiveDataList<T> {
    private MutableLiveData<List<T>> data;

    public LiveDataList() {
        data = new MutableLiveData<>();
        data.setValue(new ArrayList<>());
    }

    public List<T> getValue() {
        return data.getValue();
    }

    public void setValue(List<T> value) {
        data.setValue(value);
    }

    public void postValue(List<T> value) {
        data.setValue(value);
    }

    public Optional<T> get(int index) {
        if (data.getValue() == null) return Optional.empty();
        return Optional.of(data.getValue().get(index));
    }

    public void add(T item) {
        List<T> tempData = data.getValue();
        if (tempData == null) tempData = new ArrayList<>();
        tempData.add(item);
        data.setValue(tempData);
    }

    public void post(T item) {
        List<T> tempData = data.getValue();
        if (tempData == null) tempData = new ArrayList<>();
        tempData.add(item);
        data.postValue(tempData);
    }

    public void remove(T item) {
        List<T> tempData = data.getValue();
        if (tempData == null) tempData = new ArrayList<>();
        tempData.remove(item);
        data.setValue(tempData);
    }

    public void removePost(T item) {
        List<T> tempData = data.getValue();
        if (tempData == null) tempData = new ArrayList<>();
        tempData.remove(item);
        data.postValue(tempData);
    }

    public void clear() {
        data.setValue(new ArrayList<>());
    }
}
