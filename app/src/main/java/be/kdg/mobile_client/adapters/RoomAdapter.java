package be.kdg.mobile_client.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Room;

public class RoomAdapter extends ArrayAdapter<Room> {
    private final LayoutInflater inflater;

    public RoomAdapter(Context ctx, Room[] rooms) {
        super(ctx, -1, rooms);
        this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Room room = getItem(position);
        convertView = inflater.inflate(R.layout.list_item_room, parent, false);

        //Get head names
        assert room != null;
        TextView tvRoomName = convertView.findViewById(R.id.tvName);
        tvRoomName.setText(room.getName());
        TextView tvBuyIn = convertView.findViewById(R.id.tvBuyIn);
        tvBuyIn.setText(String.format(Locale.ENGLISH,"Buy-in: %d", room.getGameRules().getStartingChips()));

        //Get game rule information
        TextView tvBlinds = convertView.findViewById(R.id.tvBlinds);
        tvBlinds.setText(String.format(Locale.ENGLISH, "%d/%d", room.getGameRules().getSmallBlind(), room.getGameRules().getBigBlind()));
        TextView tvTimer = convertView.findViewById(R.id.tvTimer);
        tvTimer.setText(String.format(Locale.ENGLISH, "%ds", room.getGameRules().getPlayDelay()));
        TextView tvCap = convertView.findViewById(R.id.tvCap);
        tvCap.setText(String.format(Locale.ENGLISH, "%d/%d", 0, room.getGameRules().getMaxPlayerCount()));

        //Get images
        initializeDrawable(R.drawable.ic_coins, convertView.findViewById(R.id.ivCoin));
        initializeDrawable(R.drawable.ic_timer, convertView.findViewById(R.id.ivTimer));
        initializeDrawable(R.drawable.ic_not_full, convertView.findViewById(R.id.ivCap));

        return convertView;
    }

    /**
     * Util method that is used to initialize all image views inside one of the rooms.
     *
     * @param src The source of the image.
     * @param target The target where the image needs to go.
     */
    private void initializeDrawable(int src, ImageView target) {
        Picasso.get()
                .load(src)
                .resize(35, 35)
                .centerInside()
                .into(target);
    }
}
