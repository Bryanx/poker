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
import be.kdg.mobile_client.shared.validators.EmailValidator;
import be.kdg.mobile_client.shared.validators.UsernameValidator;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {
    private static final String CAN_T_REGISTER = "Can't register";
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etSearch) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.tvBroMessageRegister) TextView tvBroMessage;
    @BindView(R.id.btnRegister) Button btnRegister;
    @Inject SharedPrefService sharedPrefService;
    @Inject AuthorizationService authService;
    @Inject EmailValidator emailValidator;
    @Inject UsernameValidator usernameValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        addEventListeners();
    }

    private void addEventListeners() {
        etEmail.addTextChangedListener(emailValidator);
        etUsername.addTextChangedListener(usernameValidator);
        btnRegister.setOnClickListener(v -> register());
        tvBroMessage.setOnClickListener(e -> navigateTo(LoginActivity.class));
    }

    private void register() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (validateRegister(password)) {
            btnRegister.setEnabled(false);
            getTokenFromServer(new Credential(username, email, password));
        }
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(Credential credential) {
        compositeDisposable.add(authService.register(credential)
                .subscribe(this::onRegisterSuccess, this::onRegisterFailed));
    }

    /**
     * Gets called when user logs in successfully and closes register activity.
     */
    private void onRegisterSuccess(Token token) {
        token.setSignedIn(true);
        token.setUsername(etUsername.getText().toString());
        sharedPrefService.saveToken(this, token);
        runOnUiThread(() -> Toast.makeText(getBaseContext(), getString(R.string.logging_in), Toast.LENGTH_LONG).show());
        navigateTo(MenuActivity.class);
    }

    /**
     * Gets called when user fails to register and shows toast.
     */
    private void onRegisterFailed(Throwable throwable) {
        runOnUiThread(() -> Toast.makeText(this, getString(R.string.error_register_message), Toast.LENGTH_LONG).show());
        Log.e(CAN_T_REGISTER, throwable.getMessage());
        btnRegister.setEnabled(true);
    }


    /**
     * Validates if given credentials are correct.
     */
    private boolean validateRegister(String password) {
        if (!emailValidator.isValid()) {
            etEmail.setError(getString(R.string.error_invalid_mail));
            return false;
        }
        if (!usernameValidator.isValid()) {
            etUsername.setError(getString(R.string.error_invalid_username));
            return false;
        }
        if (password.isEmpty() || password.length() < 4) {
            etPassword.setError(getString(R.string.error_invalid_password));
            return false;
        }
        etEmail.setError(null);
        etPassword.setError(null);
        return true;
    }

}
