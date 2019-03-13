package be.kdg.mobile_client.user.settings;

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

import be.kdg.mobile_client.BaseActivity;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.MenuActivity;
import be.kdg.mobile_client.user.model.User;
import be.kdg.mobile_client.user.UserService;
import be.kdg.mobile_client.user.authorization.Credential;
import be.kdg.mobile_client.shared.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity to alter user settings
 * Able to change username, first name, last name and password.
 */
public class UserSettingsActivity extends BaseActivity {
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
        compositeDisposable.add(userService.getUser(userId).subscribe(response -> {
            runOnUiThread(() -> {
                user = response;
                etUsername.setText(response.getUsername());
                etFirstName.setText(response.getFirstname());
                etLastName.setText(response.getLastname());
                if(response.getProfilePicture() != null) {
                    byte[] decodedString = Base64.decode(response.getProfilePicture(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ivPicture.setImageBitmap(decodedByte);
                }
            });
        }, throwable -> handleError(throwable, getString(R.string.user_settings_activity), getString(R.string.error_loading_user))));
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
        if (etPassword1.getText().toString().equals(etPassword2.getText().toString())) {
            Credential authDTO = new Credential(user.getUsername(), user.getEmail(), etPassword1.getText().toString());
            compositeDisposable.add(userService.changePassword(authDTO).subscribe(response -> {
                if (response != null) {
                    onUpdateUserSuccess();
                }
            }, throwable -> handleError(throwable, getString(R.string.user_settings_activity), getString(R.string.error_updating_user))));
        }
    }

    /**
     * API call to update user
     */
    private void updateUser() {
        compositeDisposable.add(userService.changeUser(user).subscribe(response -> {
            if (response != null) {
                onUpdateUserSuccess();
            }
        }, throwable -> handleError(throwable, getString(R.string.user_settings_activity), getString(R.string.error_updating_password))));
    }

    /**
     * Gets called when user updates successfully and closes update activity.
     */
    public void onUpdateUserSuccess() {
        runOnUiThread(() -> {
            Toast.makeText(getBaseContext(), getString(R.string.update), Toast.LENGTH_LONG).show();
            btnUpdate.setEnabled(true);
        });

        setResult(RESULT_OK);
        finish();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    /**
     * Convenient method for handling errors
     */
    public void handleError(Throwable error, String tag, String message) {
        Log.e(tag, message);
        if (error != null) {
            Toast.makeText(this, error.getMessage(), android.widget.Toast.LENGTH_LONG).show();
            Log.e(tag, error.getMessage());
            error.printStackTrace();
        } else {
            Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show();
        }
    }
}
