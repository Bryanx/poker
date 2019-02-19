package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.services.GameService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class OverviewActivity extends BaseActivity {
    @BindView(R.id.btnBack)
    Button btnBack;
    @Inject
    private GameService gameService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        ButterKnife.bind(this);
        addEventHandlers();
    }

    private void addEventHandlers() {
       btnBack.setOnClickListener(e -> {
           Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
           startActivity(intent);
       });
    }
}
