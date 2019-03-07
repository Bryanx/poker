package be.kdg.mobile_client.user.authorization;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.shared.SharedPrefService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import be.kdg.mobile_client.user.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.etSearch) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.tvBroMessageLogin) TextView tvBroMessage;
    @BindView(R.id.btnLogin) Button btnLogin;
    @Inject SharedPrefService sharedPrefService;
    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        addEventListeners();
    }

    private void addEventListeners() {
        tvBroMessage.setOnClickListener(e -> navigateTo(RegisterActivity.class));
        this.btnLogin.setOnClickListener(v -> login());
    }

    /**
     * Handles user login.
     */
    public void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        btnLogin.setEnabled(false);
        getTokenFromServer(username, password);
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(String username, String password) {
        userService.login(username, password, "password").enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response != null && response.isSuccessful()) {
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
        token.setUsername(etUsername.getText().toString());
        sharedPrefService.saveToken(this, token);
        Toast.makeText(getBaseContext(), getString(R.string.logging_in), Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
        setResult(RESULT_OK);
        finish();
        navigateTo(MenuActivity.class);
    }

    /**
     * Gets called when user fails to log in and shows toast.
     */
    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), getString(R.string.error_login_message), Toast.LENGTH_LONG).show();
        Log.e("Can't login", message);
        btnLogin.setEnabled(true);
    }
}
