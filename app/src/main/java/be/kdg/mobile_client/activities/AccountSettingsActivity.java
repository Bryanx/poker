package be.kdg.mobile_client.activities;

import android.os.Bundle;
import android.widget.EditText;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountSettingsActivity extends BaseActivity {
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etFirstName) EditText etFirstName;
    @BindView(R.id.etLastName) EditText etLastName;
    @BindView(R.id.btnUpdate) EditText btnUpdate;

    @Inject SharedPrefService sharedPrefService;
    @Inject UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ButterKnife.bind(this);
        addEventListeners();
    }

    private void addEventListeners() {
        this.btnUpdate.setOnClickListener(v -> update());
    }

    private void update() {
        String username = etUsername.getText().toString();
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();

        btnUpdate.setEnabled(false);
    }
}
