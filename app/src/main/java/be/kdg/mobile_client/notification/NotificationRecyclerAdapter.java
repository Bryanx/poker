package be.kdg.mobile_client.notification;

import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;
import android.media.VolumeShaper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import be.kdg.mobile_client.R;
import lombok.AllArgsConstructor;

/**
 * An adapter that is using the recycler method.
 * The class extends the generic inner class implementation of the ViewHolder.
 */
@AllArgsConstructor
public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder> {
    private final Context ctx;
    private final List<Notification> notifications;
    private final NotificationService notificationService;

    /**
     * Inflates the layout that will be used to display each friend.
     *
     * @return a ViewHolder that is based on the inflated view.
     */
    @NonNull
    @Override
    public NotificationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_notification, parent, false);
        return new NotificationRecyclerAdapter.ViewHolder(view);
    }

    /**
     * Binds all the information of one friend to one of the view holders.
     * When clicking on the name -> navigate to user account
     * When clicking on the delete button -> delete friend
     *
     * @param holder   The holder that "holds" the views that are created so they can be recycled.
     * @param position The position in the array.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerAdapter.ViewHolder holder, int position) {
        Notification not = notifications.get(position);

        holder.tvMessage.setText(not.getMessage());
        holder.tvDate.setText(formatTime(not.getTimestamp()));
        placeImage(R.drawable.delete, holder.ivDelete);

        if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //TODO
        }

        addEventHandlers(holder, not);
    }

    /**
     * Handles when a notification needs to be delted.
     *
     * @param holder The holder that "holds" the views that are created so they can be recycled.
     * @param not    The notification that needs to be tracked.
     */
    private void addEventHandlers(ViewHolder holder, Notification not) {
        holder.ivDelete.setOnClickListener(e -> {
            notificationService.deleteNotification(not.getId()).subscribe();
            notifications.remove(not);
            Toast.makeText(ctx, "Notification removed", Toast.LENGTH_LONG).show();
            notifyDataSetChanged();
        });
    }

    /**
     * Custom time converter.
     *
     * @param timestamp The timestamp that needs to be converted.
     * @return The converted timestamp.
     */
    private String formatTime(String timestamp) {
        List<String> monthNames = new ArrayList<>(Arrays.asList(
                "January", "February", "March",
                "April", "May", "June", "July",
                "August", "September", "October",
                "November", "December"));

        String[] data = timestamp.split("T");

        // get date
        String[] date = data[0].split("-");
        String day = date[2];
        String month = date[1].startsWith("0") ? String.valueOf(date[1].charAt(1)) : date[1];
        String monthConverted = monthNames.get(Integer.parseInt(month) - 1);
        String dateConstructed = day + ' ' + monthConverted;

        // time constructed
        String[] time = data[1].split(":");
        String hours = time[0];
        String minutes = time[1];
        String timeConstructed = hours + ':' + minutes;

        return dateConstructed + ' ' + timeConstructed;
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
     * @return The size of the users list.
     */
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * This inner class will hold the layouts in memory so that
     * they can be recycled.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        TextView tvDate;
        ImageView ivDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
