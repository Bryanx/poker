package be.kdg.mobile_client.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    @BindView(R.id.tvNoNots) TextView tvNoNots;
    @BindView(R.id.lvNots) RecyclerView lvNots;
    @BindView(R.id.ivRefresh) ImageView ivRefresh;
    @BindView(R.id.btnDeleteAll) Button btnDeleteAll;
    @Inject NotificationViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        getControllerComponent().inject(this);
        placeImages();
        addEventListeners();
        getNotifications();
        view.setMinimumWidth(getResources().getDisplayMetrics().widthPixels/2);
        return view;
    }

    /**
     * Places the images into the fragment
     */
    private void placeImages() {
        Picasso.get()
                .load(R.drawable.refresh)
                .resize(30, 30)
                .centerInside()
                .into(ivRefresh);
    }

    /**
     * Adds a listener to delete all the notifications.
     */
    private void addEventListeners() {
        btnDeleteAll.setOnClickListener(e -> {
            progressBar.setVisibility(View.VISIBLE);
            viewModel.deleteAllNotifications();
            initializeAdapter(new ArrayList<>());
            Toast.makeText(getContext(), "Deleted all notifications", Toast.LENGTH_LONG).show();
        });

        ivRefresh.setOnClickListener(e -> {
            getNotifications();
            Toast.makeText(getContext(), "Refreshed data", Toast.LENGTH_LONG).show();
        });
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

    /**
     * Retrieves the notifications out of the view model.
     */
    private void getNotifications() {
        progressBar.setVisibility(View.VISIBLE);
        viewModel.getNotifications().observe(this, this::initializeAdapter);
    }

    /**
     * Initializes the notifications adapter to show all the notifications that
     * were retrieved from the user-service back-end.
     *
     * @param notifications The notifications that need to be used by the adapter.
     */
    private void initializeAdapter(List<Notification> notifications) {
        if (notifications.size() == 0) tvNoNots.setVisibility(View.VISIBLE);
        else tvNoNots.setVisibility(View.GONE);

        NotificationRecyclerAdapter adapter = new NotificationRecyclerAdapter(getContext(), notifications, viewModel.getNotificationService());
        lvNots.setAdapter(adapter);
        lvNots.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar.setVisibility(View.GONE);
    }
}
