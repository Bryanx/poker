package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Register;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.shared.CallbackWrapper;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.shared.EmailValidator;
import be.kdg.mobile_client.shared.UsernameValidator;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.etSearch) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.tvBroMessageRegister) TextView tvBroMessage;
    @BindView(R.id.btnRegister) Button btnRegister;
    @Inject SharedPrefService sharedPrefService;
    @Inject UserService userService;
    @Inject EmailValidator emailValidator;
    @Inject UsernameValidator usernameValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        addEventListners();
    }

    private void addEventListners() {
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
            getTokenFromServer(new Register(username, email, password));
        }
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(Register register) {
        userService.register(register).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response != null && response.body() != null && response.isSuccessful()) {
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
        token.setUsername(etUsername.getText().toString());
        sharedPrefService.saveToken(this, token);
        Toast.makeText(getBaseContext(), getString(R.string.logging_in), Toast.LENGTH_LONG).show();
        btnRegister.setEnabled(true);
        setResult(RESULT_OK);
        finish();
        navigateTo(MenuActivity.class);
    }

    /**
     * Gets called when user fails to regsiter and shows toast.
     */
    public void onRegisterFailed(String message) {
        Toast.makeText(getBaseContext(), getString(R.string.error_register_message), Toast.LENGTH_LONG).show();
        Log.e("Can't register", message);
        btnRegister.setEnabled(true);
    }


    /**
     * Validates if given credentials are correct.
     */
    public boolean validateRegister(String password) {
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
