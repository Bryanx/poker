package be.kdg.mobile_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.User;

/**
 * Adapter for displaying fiends.
 */
public class FriendAdapter extends ArrayAdapter<User> {
    private final LayoutInflater inflater;

    public FriendAdapter(Context ctx) {
        super(ctx, -1, new ArrayList<>());
        this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Each friend that is added to the list is styled accordingly here.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User friend = getItem(position);
        View view = inflater.inflate(R.layout.list_item_friend, parent, false);
        TextView tvFriendsRow = view.findViewById(R.id.tvFriendsRow);
        if (friend != null) {
            tvFriendsRow.setText(friend.getUsername());
        }
        return view;
    }
}
