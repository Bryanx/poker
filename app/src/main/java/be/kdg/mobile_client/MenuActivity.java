package be.kdg.mobile_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import javax.inject.Inject;

import be.kdg.mobile_client.activities.BaseActivity;
import be.kdg.mobile_client.activities.RoomActivity;
import be.kdg.mobile_client.services.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends BaseActivity {
    @BindView(R.id.logout) Button logout;
    @BindView(R.id.btnJoinGame) Button btnJoinGame;
    @Inject SharedPrefService sharedPrefService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        addEventHandlers();
    }

    private void addEventHandlers() {
        btnJoinGame.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
            startActivity(intent);
        });
        logout.setOnClickListener(e -> {
            sharedPrefService.saveToken(this, null); // remove token
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}
