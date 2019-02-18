package be.kdg.mobile_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Room;

public class RoomAdapter extends ArrayAdapter<Room> {
    private final LayoutInflater inflater;

    public RoomAdapter(Context ctx) {
        super(ctx, -1, new ArrayList<>());
        this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Room room = getItem(position);
        View view = inflater.inflate(R.layout.list_item_room, parent, false);
        return view;
    }
}
