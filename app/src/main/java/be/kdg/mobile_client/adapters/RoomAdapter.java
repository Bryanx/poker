package be.kdg.mobile_client.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.Locale;

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
        /*
        createPic(R.drawable.ic_coins).into((ImageView) convertView.findViewById(R.id.ivCoin));
        createPic(R.drawable.ic_timer).into((ImageView) convertView.findViewById(R.id.ivTimer));
        createPic(R.drawable.ic_not_full).into((ImageView) convertView.findViewById(R.id.ivCap));
*/

        return convertView;
    }

    /**
     * Util method that is used to initialize all image views inside one of the rooms.
     *
     * @param src The source of the image.
     */
    private RequestCreator createPic(int src) {
        return Picasso.get()
                .load(src)
                .resize(35, 35)
                .centerInside();
    }
}
