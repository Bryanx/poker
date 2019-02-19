package be.kdg.mobile_client.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Room;

/**
 * An adapter that is using the recycler method.
 * The class extends the generic inner class implementation of the ViewHolder.
 */
public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> {
    private List<Room> rooms;

    /**
     * Filters our all the rooms that are full before initializing the collection.
     *
     * @param rooms All the rooms.
     */
    public RoomRecyclerAdapter(List<Room> rooms) {
        List<Room> newRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getPlayersInRoom().size() < room.getGameRules().getMaxPlayerCount()) {
                newRooms.add(room);
            }
        }

        this.rooms = newRooms;
    }

    /**
     * Inflates the layout that will be used to display each room.
     *
     * @return a ViewHolder that is based on the inflated view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_room, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds all the information of one room to one of the view holders.
     *
     * @param holder   The holder that "holds" the views that are created so they can be recycled.
     * @param position The position in the array.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);

        holder.tvRoomName.setText(rooms.get(position).getName());
        holder.tvBuyIn.setText(String.format(Locale.ENGLISH, "Buy-in: %d", room.getGameRules().getStartingChips()));
        holder.tvBlinds.setText(String.format(Locale.ENGLISH, "%d/%d", room.getGameRules().getSmallBlind(), room.getGameRules().getBigBlind()));
        holder.tvTimer.setText(String.format(Locale.ENGLISH, "%ds", room.getGameRules().getPlayDelay()));
        holder.tvCap.setText(String.format(Locale.ENGLISH, "%d/%d", room.getPlayersInRoom().size(), room.getGameRules().getMaxPlayerCount()));
    }

    /**
     * Used internally by the recycler to determine how many holders should be created.
     *
     * @return The size of the rooms list.
     */
    @Override
    public int getItemCount() {
        return rooms.size();
    }

    /**
     * This inner class will hold the images in memory so that
     * they can mee recycled.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName;
        TextView tvBuyIn;
        TextView tvBlinds;
        TextView tvTimer;
        TextView tvCap;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvName);
            tvBuyIn = itemView.findViewById(R.id.tvBuyIn);
            tvBlinds = itemView.findViewById(R.id.tvBlinds);
            tvTimer = itemView.findViewById(R.id.tvTimer);
            tvCap = itemView.findViewById(R.id.tvCap);
        }
    }
}
