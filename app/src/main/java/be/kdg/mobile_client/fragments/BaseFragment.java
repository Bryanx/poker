package be.kdg.mobile_client.fragments;

import java.util.Objects;

import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.dagger.components.ControllerComponent;
import be.kdg.mobile_client.dagger.modules.ControllerModule;

/**
 * Base class for all fragments that require dependency injection.
 * Calling getControllerComponent().inject(this) will bind the fragment to the
 * Component modules, allowing access to all depencies.
 */
public class BaseFragment extends Fragment {

    private boolean controllercomponentInUse = false;

    @UiThread
    ControllerComponent getControllerComponent() {
        if (controllercomponentInUse) {
            throw new IllegalStateException("must not use ControllerComponent more than once");
        }
        controllercomponentInUse = true;
        return ((App) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(getActivity()));
    }
}
