package be.kdg.mobile_client.services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Wrapper class for retrofit enqueue callback's.
 * @param <T> return type
 */
public class CallbackWrapper<T> implements Callback<T> {
    private Wrapper<T> wrapper;

    public CallbackWrapper(Wrapper<T> wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        wrapper.onResult(t, null);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        wrapper.onResult(null, response);
    }

    public static interface Wrapper<T> {
        void onResult(Throwable t, Response<T> response);
    }
}