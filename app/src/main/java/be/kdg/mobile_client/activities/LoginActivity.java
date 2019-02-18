package be.kdg.mobile_client.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.services.CallbackWrapper;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @Inject SharedPrefService sharedPrefService;
    @Inject UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.btnLogin.setOnClickListener(v -> login());
    }

    /**
     * Handles user login.
     */
    public void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (validateLogin(username, password)) {
            btnLogin.setEnabled(false);
            getTokenFromServer(username, password);
        }
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(String username, String password) {
        userService.login(username, password, "password").enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful()) {
                onLoginSuccess(response.body());
            } else {
                onLoginFailed(throwable == null ? "" : throwable.getMessage());
            }
        }));
    }

    /**
     * Gets called when user logs in successfully and closes login activity.
     */
    public void onLoginSuccess(Token token) {
        token.setSignedIn(true);
        sharedPrefService.saveToken(getApplicationContext(), token);
        Toast.makeText(getBaseContext(), getResources().getString(R.string.logging_in), Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Gets called when user fails to log in and shows toast.
     */
    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.error_login_message), Toast.LENGTH_LONG).show();
        Log.e("Can't login", message);
        btnLogin.setEnabled(true);
    }

    /**
     * Validates if given credentials are correct.
     */
    public boolean validateLogin(String email, String password) {
        if (email.isEmpty() || email.length() < 4) {
            etUsername.setError(getResources().getString(R.string.error_invalid_mail));
            return false;
        }
        if (password.isEmpty() || password.length() < 4) {
            etPassword.setError(getResources().getString(R.string.error_invalid_password));
            return false;
        }
        etUsername.setError(null);
        etPassword.setError(null);
        return true;
    }
}
