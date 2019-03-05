package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Register;
import be.kdg.mobile_client.services.SharedPrefService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.ivLogo) ImageView ivLogo;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnSignUp) Button btnSignUp;
    @Inject SharedPrefService sharedPrefService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        redirectIfSignedIn();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initialiseView();
        addEventHandlers();
    }

    private void initialiseView() {
        Picasso.get()
                .load(R.drawable.logo_white)
                .resize(700, 400)
                .centerInside()
                .into(this.ivLogo);
    }

    private void addEventHandlers() {
        btnLogin.setOnClickListener(v -> navigateTo(LoginActivity.class));
        btnSignUp.setOnClickListener(v -> navigateTo(RegisterActivity.class));
    }

    /**
     * If the user is in this activity, check if it is signed in and redirect it.
     */
    @Override
    protected void onResume() {
        super.onResume();
        redirectIfSignedIn();
    }

    private void redirectIfSignedIn() {
        if (sharedPrefService.hasToken(this)) {
            finish();
            navigateTo(MenuActivity.class);
        }
    }

}
