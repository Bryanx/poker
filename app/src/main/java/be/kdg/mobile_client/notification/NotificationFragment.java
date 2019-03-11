package be.kdg.mobile_client.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.App;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.shared.di.components.ControllerComponent;
import be.kdg.mobile_client.shared.di.modules.ControllerModule;
import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotificationFragment extends Fragment {
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.lvNots) RecyclerView lvNots;
    @Inject NotificationViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        getControllerComponent().inject(this);
        getNotifications();
        return view;
    }

    /**
     * Retrieve ControllerComponent so services become injectable.
     */
    @UiThread
    protected ControllerComponent getControllerComponent() {
        return ((App) Objects.requireNonNull(getActivity()).getApplication())
                .getAppComponent()
                .newControllerComponent(new ControllerModule(getActivity()));
    }

    private void getNotifications() {
        viewModel.getNotifications().observe(this, this::initializeAdapter);
    }

    private void initializeAdapter(List<Notification> notifications) {
        progressBar.setVisibility(View.GONE);
        NotificationRecyclerAdapter adapter = new NotificationRecyclerAdapter(getContext(), notifications);
        lvNots.setAdapter(adapter);
        lvNots.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
