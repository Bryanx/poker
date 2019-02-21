package be.kdg.mobile_client.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import javax.inject.Inject;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.User;
import be.kdg.mobile_client.services.CallbackWrapper;
import be.kdg.mobile_client.services.UserService;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Activity for displaying ranks of all players.
 * The list of ranks is fed into a TableLayout containing TableRows.
 */
public class RankingsActivity extends BaseActivity {
    @BindView(R.id.tblRankings) TableLayout tblRankings;
    @Inject UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getControllerComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);
        ButterKnife.bind(this);
        fetchRankings();
    }

    private void fetchRankings() {
        userService.getUsers().enqueue(new CallbackWrapper<>((throwable, response) -> {
            if (response.isSuccessful() && response.body() != null) {
                loadRankingsIntoView(response.body());
            }
        }));
    }

    private void loadRankingsIntoView(User[] users) {
        for (int i = 0, usersLength = users.length; i < usersLength; i++) {
            User user = users[i];
            TableRow row = new TableRow(this);
            row.addView(newTextView(String.valueOf(i + 1) + '.'));
            row.addView(newTextView(user.getUsername()));
            row.addView(newTextView(String.valueOf(user.getChips())));
            row.addView(newTextView(String.valueOf(user.getWins())));
            row.addView(newTextView(String.valueOf(user.getGamesPlayed() - user.getWins())));
            row.addView(newTextView(String.valueOf(user.getGamesPlayed())));
            addOnClickListener(row, user);
            tblRankings.addView(row, i + 1);
        }
    }

    /**
     * Returns a new textview and gives the style of rankings-row.
     */
    private View newTextView(String content) {
        TextView tv = new TextView(new ContextThemeWrapper(this, R.style.rankingsDataRow));
        tv.setText(content);
        return tv;
    }

    /**
     * Upon clicking on a specific row in the table, you are redirected to the AccountActivity
     */
    private void addOnClickListener(TableRow row, User user) {
        row.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
            intent.putExtra(getString(R.string.userid), user.getId());
            startActivity(intent);
        });
    }
}
