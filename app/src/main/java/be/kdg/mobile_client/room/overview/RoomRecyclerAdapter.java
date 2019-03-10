package be.kdg.mobile_client.room.overview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.room.Room;
import be.kdg.mobile_client.room.RoomActivity;
import be.kdg.mobile_client.room.RoomService;
import be.kdg.mobile_client.user.User;
import lombok.AllArgsConstructor;

/**
 * An adapter that is using the recycler method.
 * The class extends the generic inner class implementation of the ViewHolder.
 */
@AllArgsConstructor
public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> {
    private final User myself;
    private final RoomService roomService;
    private Context ctx;
    private List<Room> rooms;
    private boolean edit;

    /**
     * Filters out all the rooms that are full before initializing the collection.
     *
     * @param rooms All the rooms.
     */
    RoomRecyclerAdapter(Context ctx, List<Room> rooms, User user, RoomService roomService, boolean edit) {
        this.ctx = ctx;
        this.myself = user;
        this.edit = edit;
        this.roomService = roomService;
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

        holder.roomCard.setEnabled(true);
        holder.tvRoomName.setText(rooms.get(position).getName());
        holder.tvBuyIn.setText(String.format(Locale.ENGLISH, "Buy-in: %d", room.getGameRules().getStartingChips()));
        holder.tvBlinds.setText(String.format(Locale.ENGLISH, "%d/%d", room.getGameRules().getSmallBlind(), room.getGameRules().getBigBlind()));
        holder.tvTimer.setText(String.format(Locale.ENGLISH, "%ds", room.getGameRules().getPlayDelay()));
        holder.tvCap.setText(String.format(Locale.ENGLISH, "%d/%d", room.getPlayersInRoom().size(), room.getGameRules().getMaxPlayerCount()));

        placeImage(R.drawable.coins, holder.ivCoin);
        placeImage(R.drawable.timer, holder.ivTimer);
        placeImage(R.drawable.not_full, holder.ivCap);
        placeImage(R.drawable.delete, holder.ivDelete);
        if (!edit) holder.ivDelete.setVisibility(View.GONE);

        addEventListeners(holder, room);
    }

    /**
     * Adds the appropriate event listeners to some of the items in the view holder.
     *
     * @param holder The items in the holder that need to be linked to a listener.
     * @param room The room that will correspond with those listeners.
     */
    private void addEventListeners(@NonNull ViewHolder holder, Room room) {
        holder.roomCard.setOnClickListener(e -> {
            holder.roomCard.setEnabled(false);
            if (room.getGameRules().getStartingChips() > myself.getChips()) {
                Toast.makeText(ctx, "You don't have enough chips.", Toast.LENGTH_LONG).show();
            } else if (room.getPlayersInRoom().size() >= room.getGameRules().getMaxPlayerCount()) {
                Toast.makeText(ctx, "Room is full.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(ctx, RoomActivity.class);
                intent.putExtra(ctx.getString(R.string.room_id), room.getId());
                ctx.startActivity(intent);
            }
        });

        holder.ivDelete.setOnClickListener(e -> {
            roomService.deleteRoom(room.getId()).subscribe();
            rooms.remove(room);
            notifyDataSetChanged();
        });
    }


    /**
     * Places an image inside the image view.
     *
     * @param src    The source of the image.
     * @param target The target where the source needs to be placed.
     */
    private void placeImage(int src, ImageView target) {
        Picasso.get()
                .load(src)
                .resize(35, 35)
                .centerInside()
                .into(target);
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
     * they can be recycled.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        CardView roomCard;
        TextView tvRoomName;
        TextView tvBuyIn;
        TextView tvBlinds;
        TextView tvTimer;
        TextView tvCap;
        ImageView ivCoin;
        ImageView ivTimer;
        ImageView ivCap;
        ImageView ivDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvName);
            tvBuyIn = itemView.findViewById(R.id.tvBuyIn);
            tvBlinds = itemView.findViewById(R.id.tvBlinds);
            tvTimer = itemView.findViewById(R.id.tvTimer);
            tvCap = itemView.findViewById(R.id.tvCap);
            ivCoin = itemView.findViewById(R.id.ivCoin);
            ivTimer = itemView.findViewById(R.id.ivTimer);
            ivCap = itemView.findViewById(R.id.ivCap);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            roomCard = itemView.findViewById(R.id.roomCard);
        }
    }
}
