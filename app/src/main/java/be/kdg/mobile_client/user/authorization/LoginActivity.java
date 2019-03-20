package be.kdg.mobile_client.user.authorization;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import androidx.annotation.UiThread;
import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.shared.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.HttpException;

public class LoginActivity extends BaseActivity {
    private static final String CAN_T_LOGIN = "Can't login";
    private static final String PASSWORD = "password";
    @BindView(R.id.etSearch) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.tvBroMessageLogin) TextView tvBroMessage;
    @BindView(R.id.btnLogin) Button btnLogin;
    @Inject AuthorizationService authService;
    @Inject SharedPrefService sharedPrefService;

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
    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        btnLogin.setEnabled(false);
        getTokenFromServer(username, password);
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(String username, String password) {
        compositeDisposable.add(authService.login(username, password, PASSWORD)
                .subscribe(this::onLoginSuccess, this::onLoginFailed));
    }

    /**
     * Gets called when user logs in successfully and closes login activity.
     */
    private void onLoginSuccess(Token token) {
        token.setSignedIn(true);
        token.setUsername(etUsername.getText().toString());
        sharedPrefService.saveToken(this, token);
        runOnUiThread(() -> Toast.makeText(this, getString(R.string.logging_in), Toast.LENGTH_LONG).show());
        navigateTo(MenuActivity.class);
    }

    /**
     * Gets called when user fails to log in and shows toast.
     */
    @UiThread
    private void onLoginFailed(Throwable throwable) {
        runOnUiThread(() -> {
            String toastMessage = throwable.getMessage();
            if (throwable instanceof HttpException) {
                HttpException error = (HttpException) throwable;
                switch (error.response().code()) {
                    case 401: toastMessage = getString(R.string.incorrect_username_or_password); break;
                    case 503: toastMessage = getString(R.string.service_unavailable); break;
                    case 500: toastMessage = getString(R.string.internal_server_error); break;
                }
            }
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
            Log.e(CAN_T_LOGIN, throwable.getMessage());
            btnLogin.setEnabled(true);
        });
    }
}
