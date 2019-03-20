package be.kdg.mobile_client.shared;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Provides a new viewmodel (at the moment only to the controllermodule)
 * @param <V> ViewModel class
 */
public class ViewModelProviderFactory<V> implements ViewModelProvider.Factory {
    private static final String UNKNOWN_CLASS_NAME = "Unknown class name";
    private final V viewModel;

    public ViewModelProviderFactory(V viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(viewModel.getClass())) {
            return (T) viewModel;
        }
        throw new IllegalArgumentException(UNKNOWN_CLASS_NAME);
    }
}