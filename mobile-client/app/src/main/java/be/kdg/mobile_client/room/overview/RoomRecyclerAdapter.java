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

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.room.RoomActivity;
import be.kdg.mobile_client.room.RoomService;
import be.kdg.mobile_client.room.model.Room;
import be.kdg.mobile_client.user.model.User;
import lombok.AllArgsConstructor;

/**
 * An adapter that is using the recycler method.
 * The class extends the generic inner class implementation of the ViewHolder.
 */
@AllArgsConstructor
public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> {
    private final User myself;
    private final RoomService roomService;
    private final Context ctx;
    private final List<Room> rooms;
    private final boolean edit;
    private final boolean isPublic;

    /**
     * Filters out all the rooms that are full before initializing the collection.
     *
     * @param rooms All the rooms.
     */
    RoomRecyclerAdapter(Context ctx, List<Room> rooms, User user, RoomService roomService, boolean edit, boolean isPublic) {
        this.ctx = ctx;
        this.myself = user;
        this.edit = edit;
        this.isPublic = isPublic;
        this.roomService = roomService;
        this.rooms = rooms.stream()
                .filter((room -> room.getPlayersInRoom().size() < room.getGameRules().getMaxPlayerCount()))
                .filter(room -> room.getGameRules().getMaxLevel() > user.getLevel())
                .collect(Collectors.toList());
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
        holder.tvLevels.setText(String.format(Locale.ENGLISH, "%d - %d", room.getGameRules().getMinLevel(), room.getGameRules().getMaxLevel()));
        holder.tvBuyIn.setText(String.format(Locale.ENGLISH, "Buy-in: %d", room.getGameRules().getStartingChips()));
        holder.tvBlinds.setText(String.format(Locale.ENGLISH, "%d/%d", room.getGameRules().getSmallBlind(), room.getGameRules().getBigBlind()));
        holder.tvTimer.setText(String.format(Locale.ENGLISH, "%ds", room.getGameRules().getPlayDelay()));
        holder.tvCap.setText(String.format(Locale.ENGLISH, "%d/%d", room.getPlayersInRoom().size(), room.getGameRules().getMaxPlayerCount()));

        placeImage(R.drawable.coins, holder.ivCoin);
        placeImage(R.drawable.timer, holder.ivTimer);
        placeImage(R.drawable.not_full, holder.ivCap);
        placeImage(R.drawable.delete, holder.ivDelete);

        if (!edit) holder.ivDelete.setVisibility(View.GONE);
        if (!isPublic) holder.tvLevels.setVisibility(View.GONE);
        if (room.getGameRules().getMinLevel() > myself.getLevel() && isPublic) {
            holder.ivLock.setVisibility(View.VISIBLE);
            addDisableEventListeners(holder);
        } else addEventListeners(holder, room);
    }

    /**
     * Adds a event listener to notify the user that he is to low of a level to join
     * the room.
     *
     * @param holder The holder that "holds" the views that are created so they can be recycled.
     */
    private void addDisableEventListeners(ViewHolder holder) {
        holder.roomCard.setOnClickListener(e ->
                Toast.makeText(ctx, ctx.getString(R.string.this_room_is_locked), Toast.LENGTH_LONG).show()
        );
    }

    /**
     * Adds the appropriate event listeners to some of the items in the view holder.
     *
     * @param holder The items in the holder that need to be linked to a listener.
     * @param room   The room that will correspond with those listeners.
     */
    private void addEventListeners(@NonNull ViewHolder holder, Room room) {
        holder.roomCard.setOnClickListener(e -> {
            holder.roomCard.setEnabled(false);
            if (room.getGameRules().getStartingChips() > myself.getChips()) {
                Toast.makeText(ctx, ctx.getString(R.string.you_dont_have_enough_chips), Toast.LENGTH_LONG).show();
            } else if (room.getPlayersInRoom().size() >= room.getGameRules().getMaxPlayerCount()) {
                Toast.makeText(ctx, ctx.getString(R.string.room_is_full), Toast.LENGTH_LONG).show();
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
            Toast.makeText(ctx, ctx.getString(R.string.deleted_roomname, room.getName()), Toast.LENGTH_LONG).show();
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
        final CardView roomCard;
        final TextView tvRoomName;
        final TextView tvBuyIn;
        final TextView tvBlinds;
        final TextView tvTimer;
        final TextView tvCap;
        final TextView tvLevels;
        final ImageView ivCoin;
        final ImageView ivTimer;
        final ImageView ivCap;
        final ImageView ivDelete;
        final View ivLock;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvName);
            tvBuyIn = itemView.findViewById(R.id.tvBuyIn);
            tvBlinds = itemView.findViewById(R.id.tvBlinds);
            tvTimer = itemView.findViewById(R.id.tvTimer);
            tvCap = itemView.findViewById(R.id.tvCap);
            tvLevels = itemView.findViewById(R.id.tvLevels);
            ivCoin = itemView.findViewById(R.id.ivCoin);
            ivTimer = itemView.findViewById(R.id.ivTimer);
            ivCap = itemView.findViewById(R.id.ivCap);
            ivLock = itemView.findViewById(R.id.ivLock);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            roomCard = itemView.findViewById(R.id.roomCard);
        }
    }
}
