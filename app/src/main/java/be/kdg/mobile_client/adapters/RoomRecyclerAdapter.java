package be.kdg.mobile_client.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.Room;
import lombok.AllArgsConstructor;

/**
 * An adapter that is using the recycler method.
 */
@AllArgsConstructor
public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> {
    private List<Room> rooms;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.tvRoomName.setText(rooms.get(position).getName());
        holder.tvBuyIn.setText(String.format(Locale.ENGLISH, "Buy-in: %d", room.getGameRules().getStartingChips()));
        holder.tvBlinds.setText(String.format(Locale.ENGLISH, "%d/%d", room.getGameRules().getSmallBlind(), room.getGameRules().getBigBlind()));
        holder.tvTimer.setText(String.format(Locale.ENGLISH, "%ds", room.getGameRules().getPlayDelay()));
        holder.tvCap.setText(String.format(Locale.ENGLISH, "%d/%d", room.getPlayersInRoom().size(), room.getGameRules().getMaxPlayerCount()));
    }

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
