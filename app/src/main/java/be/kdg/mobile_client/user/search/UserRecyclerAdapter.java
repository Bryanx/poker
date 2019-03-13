package be.kdg.mobile_client.user.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.notification.Notification;
import be.kdg.mobile_client.notification.NotificationType;
import be.kdg.mobile_client.notification.NotificationViewModel;
import be.kdg.mobile_client.user.model.User;
import be.kdg.mobile_client.user.UserActivity;
import lombok.AllArgsConstructor;

/**
 * An adapter that is using the recycler method.
 * The class extends the generic inner class implementation of the ViewHolder.
 */
@AllArgsConstructor
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {
    private final Context ctx;
    private final List<User> users;
    private final User myself;
    private final NotificationViewModel viewModel;

    /**
     * Inflates the layout that will be used to display each user.
     *
     * @return a ViewHolder that is based on the inflated view.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds all the information of one user to one of the view holders.
     *
     * @param holder   The holder that "holds" the views that are created so they can be recycled.
     * @param position The position in the array.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvName.setText(user.getUsername());

        myself.getFriends().stream()
                .filter(friend -> friend.getUserId() != null)
                .filter(friend -> friend.getUserId().equals(user.getId()))
                .findAny()
                .ifPresent(friend -> holder.btnAdd.setVisibility(View.GONE));
        if (user.getId().equals(myself.getId())) holder.btnAdd.setVisibility(View.GONE);

        addEventListeners(holder, user);
    }

    /**
     * @param holder The holder that "holds" the views that are created so they can be recycled.
     * @param user   The user that needs to correspond with the listeners.
     */
    private void addEventListeners(ViewHolder holder, User user) {
        holder.userCard.setOnClickListener(e -> {
            Intent intent = new Intent(ctx, UserActivity.class);
            intent.putExtra(ctx.getString(R.string.userid), user.getId());
            ctx.startActivity(intent);
        });

        holder.btnAdd.setOnClickListener(e -> {
            if (ctx instanceof UserSearchActivity) {
                ((UserSearchActivity) ctx).addFriend(user);
            }
            holder.btnAdd.setVisibility(View.GONE);
            Notification not = new Notification();
            not.setMessage(myself.getUsername() + " has sent you a friend request");
            not.setRef(myself.getId());
            not.setType(NotificationType.FRIEND_REQUEST);
            viewModel.sendNotification(user.getId(), not);
            Toast.makeText(ctx, "Befriended " + user.getUsername(), Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Used internally by the recycler to determine how many holders should be created.
     *
     * @return The size of the users list.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * This inner class will hold the layouts in memory so that
     * they can be recycled.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        Button btnAdd;
        CardView userCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            userCard = itemView.findViewById(R.id.userCard);
        }
    }
}
