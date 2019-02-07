package be.kdg.mobile_client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import be.kdg.mobile_client.services.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends AppCompatActivity {

    @BindView(R.id.logout) Button logout;
    @BindView(R.id.btnJoinGame) Button btnJoinGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            SharedPrefService.saveToken(this, null); // remove token
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }
}
