package be.kdg.mobile_client.user.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import be.kdg.mobile_client.user.User;
import be.kdg.mobile_client.user.UserActivity;
import lombok.AllArgsConstructor;

/**
 * An adapter that is using the recycler method.
 * The class extends the generic inner class implementation of the ViewHolder.
 */
@AllArgsConstructor
public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {
    private Context ctx;
    private List<User> users;

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
        holder.tvName.setOnClickListener(e -> {
            Intent intent = new Intent(ctx, UserActivity.class);
            intent.putExtra(ctx.getString(R.string.userid), user.getId());
            ctx.startActivity(intent);
        });
        holder.btnAdd.setOnClickListener(e -> {
            if (ctx instanceof  UserSearchActivity) {
                ((UserSearchActivity) ctx).addFriend(user);
            }
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
