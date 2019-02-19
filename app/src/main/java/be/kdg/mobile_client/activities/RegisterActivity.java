package be.kdg.mobile_client.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Register;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.services.CallbackWrapper;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnRegister) Button btnRegister;

    @Inject
    SharedPrefService sharedPrefService;

    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        this.btnRegister.setOnClickListener(v -> register());
    }

    private void register() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (validateRegister(email, password)) {
            btnRegister.setEnabled(false);
            getTokenFromServer(new Register(username, email, password));
        }
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(Register registerDTO) {
        userService.register(registerDTO).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful()) {
                onRegisterSuccess(response.body());
            } else {
                onRegisterFailed(throwable == null ? "" : throwable.getMessage());
            }
        }));
    }

    /**
     * Gets called when user logs in successfully and closes register activity.
     */
    public void onRegisterSuccess(Token token) {
        token.setSignedIn(true);
        sharedPrefService.saveToken(getApplicationContext(), token);
        Toast.makeText(getBaseContext(), getResources().getString(R.string.logging_in), Toast.LENGTH_LONG).show();
        btnRegister.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Gets called when user fails to regsiter and shows toast.
     */
    public void onRegisterFailed(String message) {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.error_register_message), Toast.LENGTH_LONG).show();
        Log.e("Can't register", message);
        btnRegister.setEnabled(true);
    }


    /**
     * Validates if given credentials are correct.
     */
    public boolean validateRegister(String email, String password) {
        if (email.isEmpty() || email.length() < 4) {
            etEmail.setError(getResources().getString(R.string.error_invalid_mail));
            return false;
        }
        if (password.isEmpty() || password.length() < 4) {
            etPassword.setError(getResources().getString(R.string.error_invalid_password));
            return false;
        }
        etEmail.setError(null);
        etPassword.setError(null);
        return true;
    }

}
