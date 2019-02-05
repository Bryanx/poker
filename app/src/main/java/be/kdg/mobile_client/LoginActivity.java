package be.kdg.mobile_client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.services.ServiceGenerator;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    UserService userService = ServiceGenerator.createService(UserService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.btnLogin.setOnClickListener(v -> login());
    }

    /**
     * Handles user login.
     */
    public void login() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (validate(email, password)) {
            btnLogin.setEnabled(false);
            getTokenFromServer(email, password);
        }
    }

    /**
     * Retrieves token from backend with a POST request.
     */
    private void getTokenFromServer(String username, String password) {
        userService.login(username, password, "password").enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    writeSharedPrefs(response.body());
                    onLoginSuccess();
                } else {
                    onLoginFailed("");
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable e) {
                onLoginFailed(e.getMessage());
            }
        });
    }

    /**
     * Gets called when user logs in successfully and closes login activity.
     */
    public void onLoginSuccess() {
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
    public boolean validate(String email, String password) {
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

    /**
     * Writes token to shared preferences for later use.
     */
    private void writeSharedPrefs(Token token) {
        SharedPreferences sharedPref = (LoginActivity.this).getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String tokenJson = gson.toJson(token);
        editor.putString(getString(R.string.token), tokenJson);
        editor.apply();
    }
}
