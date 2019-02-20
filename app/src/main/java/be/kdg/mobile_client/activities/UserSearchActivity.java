package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import be.kdg.mobile_client.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserSearchActivity extends BaseActivity {
    @BindView(R.id.btnBack) Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersearch);
        ButterKnife.bind(this);
        addEventHandlers();
    }

    private void addEventHandlers() {
        btnBack.setOnClickListener(e -> {
            Intent intent = new Intent(this, FriendsActivity.class);
            startActivity(intent);
        });
    }
}
