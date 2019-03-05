package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Register;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.services.SharedPrefService;
import be.kdg.mobile_client.services.UserService;
import be.kdg.mobile_client.shared.CallbackWrapper;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

/**
 * Activity to alter user settings
 * Able to change username, first name, last name and password.
 */
public class AccountSettingsActivity extends BaseActivity {
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etFirstName) EditText etFirstName;
    @BindView(R.id.etLastName) EditText etLastName;
    @BindView(R.id.btnUpdate) Button btnUpdate;
    @BindView(R.id.ivPicture) ImageView ivPicture;

    @BindView(R.id.etFirstPassword) EditText etPassword1;
    @BindView(R.id.etSecondPassword) EditText etPassword2;
    @BindView(R.id.btnUpdatePassword) Button btnUpdatePassword;

    @Inject SharedPrefService sharedPrefService;
    @Inject UserService userService;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        checkIfAuthorized(sharedPrefService);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        ButterKnife.bind(this);
        this.getUser();
        addEventListeners();
    }

    private void getUser() {
        String userId = sharedPrefService.getUserId(this);
        userService.getUser(userId).enqueue(new CallbackWrapper<>(this::onResult));
    }

    private boolean responseSuccess(Response response) {
        return response != null && response.body() != null && response.isSuccessful();
    }

    private void handleError(Throwable throwable, String tag, String msg) {
        if (throwable != null) Log.e(tag, throwable.getMessage());
    }

    private void addEventListeners() {
        this.btnUpdate.setOnClickListener(v -> update());
        this.btnUpdatePassword.setOnClickListener(v -> updatePassword());
    }

    /**
     * Update user object with new username, firstname and lastname
     */
    private void update() {
        user.setUsername(etUsername.getText().toString());
        user.setFirstname(etFirstName.getText().toString());
        user.setLastname(etLastName.getText().toString());
        btnUpdate.setEnabled(false);

        this.updateUser();
    }

    private void updatePassword() {
        // Check if passwords match
        if(etPassword1.getText().toString().equals(etPassword2.getText().toString())) {
            Register authDTO = new Register(user.getUsername(), user.getEmail(), etPassword1.getText().toString());
            userService.changePassword(authDTO).enqueue(new CallbackWrapper<>((throwable, response) -> {
                if (responseSuccess(response)) {
                    onUpdateUserSuccess();
                } else {
                    handleError(throwable, "changeUser", "Error updating user");
                }
            }));
        }
    }

    /**
     * API call to update user
     */
    private void updateUser() {
        userService.changeUser(user).enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (responseSuccess(response)) {
                onUpdateUserSuccess();
            } else {
                handleError(throwable, "changeUser", "Error updating password for user");
            }
        }));
    }

    /**
     * Gets called when user object is received
     */
    private void onResult(Throwable throwable, Response<User> response) {
        if (responseSuccess(response)) {
            user = response.body();
            etUsername.setText(user.getUsername());
            etFirstName.setText(user.getFirstname());
            etLastName.setText(user.getLastname());
            byte[] decodedString = Base64.decode(user.getProfilePicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivPicture.setImageBitmap(decodedByte);
        } else {
            handleError(throwable, "loadUser", "Error loading user");
        }
    }

    /**
     * Gets called when user updates successfully and closes update activity.
     */
    public void onUpdateUserSuccess() {
        Toast.makeText(getBaseContext(), getString(R.string.update), Toast.LENGTH_LONG).show();
        btnUpdate.setEnabled(true);
        setResult(RESULT_OK);
        finish();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
