package be.kdg.mobile_client.fragments;

import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.dagger.ControllerComponent;
import be.kdg.mobile_client.dagger.ControllerModule;

public class BaseFragment extends Fragment {

    private boolean controllercomponentInUse = false;

    @UiThread
    protected ControllerComponent getControllerComponent() {
        if (controllercomponentInUse) {
            throw new IllegalStateException("must not use ControllerComponent more than once");
        }
        controllercomponentInUse = true;
        return ((App) getActivity().getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(getActivity()));
    }
}
