package be.kdg.mobile_client.chat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import be.kdg.mobile_client.R;

/**
 * Adapter for chat messages.
 */
public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    private final LayoutInflater inflater;
    private String name;

    public ChatMessageAdapter(Context ctx) {
        super(ctx, -1, new ArrayList<>());
        this.inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Each chat message that is added to the list is styled accordingly here.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage message = getItem(position);
        View view = inflater.inflate(R.layout.list_item_chat, parent, false);
        TextView tvMessage = view.findViewById(R.id.tvMessage);
        LinearLayout llMessage = view.findViewById(R.id.llMessage);
        Resources res = getContext().getResources();
        if (message.getName().equals(name)) { // user message
            llMessage.setGravity(Gravity.END);
            tvMessage.setBackground(res.getDrawable(R.drawable.rounded_corners));
            tvMessage.setText(message.getContent());
        } else if (message.getName().equals("system")) { // system message
            llMessage.setGravity(Gravity.CENTER);
            tvMessage.setTextColor(res.getColor(R.color.colorWhite));
            tvMessage.setText(message.getContent());
            tvMessage.setBackgroundColor(Color.TRANSPARENT);
        } else if (message.getName().equals("error")) { // error message
            llMessage.setGravity(Gravity.CENTER);
            tvMessage.setTextColor(res.getColor(R.color.colorRed));
            tvMessage.setText(message.getContent());
            tvMessage.setBackgroundColor(Color.TRANSPARENT);
        } else {  // message from other users
            tvMessage.setText(String.format("%s: %s", message.getName(), message.getContent()));
        }
        return view;
    }

    public void setName(String name) {
        this.name = name;
    }
}
