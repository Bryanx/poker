package be.kdg.mobile_client.activities;

import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Named;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.databinding.ActivityAccountBinding;
import be.kdg.mobile_client.viewmodels.UserViewModel;

/**
 * Activity for player accounts. Each player has its own 'page'.
 * The user's data is fetched from the server and loaded in the view components.
 */
public class AccountActivity extends BaseActivity {
    @Inject @Named("UserViewModel") ViewModelProvider.Factory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        UserViewModel viewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        ActivityAccountBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_account);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.getUser(getIntent().getStringExtra(getString(R.string.userid)));
    }
}
