package be.kdg.mobile_client.shared;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.adapters.SeekBarBindingAdapter;
import be.kdg.mobile_client.R;

/**
 * Extends several databinding keywords in layout files.
 */
public class DataBindingAdapters {

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String base64image) {
        if (base64image == null) {
            view.setImageURI(null);
        } else {
            byte[] decodedString = Base64.decode(base64image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            view.setImageBitmap(decodedByte);
        }
    }

    @BindingAdapter("app:card_text")
    public static void setCardRank(TextView view, String cardType) {
        if (cardType == null) return;
        String rank = "";
        switch (cardType.split("_")[0].toLowerCase()) {
            case "two": rank = "2"; break;
            case "three": rank = "3"; break;
            case "four": rank = "4"; break;
            case "five": rank = "5"; break;
            case "six": rank = "6"; break;
            case "seven": rank = "7"; break;
            case "eight": rank = "8"; break;
            case "nine": rank = "9"; break;
            case "ten": rank = "10"; break;
            case "jack": rank = "J"; break;
            case "queen": rank = "Q"; break;
            case "king": rank = "K"; break;
            case "ace": rank = "A"; break;
        }
        view.setText(rank);
    }

    @BindingAdapter("app:card_suit")
    public static void setCardSuit(ImageView view, String cardType) {
        if (cardType == null) return;
        StringBuilder type = new StringBuilder("card_symbol_");
        type.append(cardType.split("_")[2].toLowerCase());
        int resourceId = view.getResources().getIdentifier(type.append("_small").toString(), "drawable",
                view.getContext().getPackageName());
        view.setImageResource(resourceId);
    }

    @BindingAdapter("app:card_text_color")
    public static void setCardColor(TextView view, String cardType) {
        if (cardType == null || cardType.contains("SPADES") || cardType.contains("CLUBS")) {
            view.setTextColor(view.getContext().getColor(R.color.colorBlack));
        } else {
            view.setTextColor(view.getContext().getColor(R.color.colorRed));
        }
    }

    @BindingAdapter("app:player_status_color")
    public static void setPlayerStatusColor(TextView view, String cardType) {
        if (cardType == null) {
            view.setTextColor(view.getContext().getColor(R.color.colorTransparent));
            return;
        }
        int resourceId = R.color.colorTransparent;
        switch (cardType) {
            case "FOLD": resourceId = R.color.colorRed;break;
            case "CALL": resourceId = R.color.colorGreen;break;
            case "CHECK": resourceId = R.color.colorGreen;break;
            case "RAISE": resourceId = R.color.colorYellow;break;
            case "BET": resourceId = R.color.colorYellow;break;
            case "UNDECIDED": resourceId = R.color.colorTransparent;
        }
        view.setTextColor(view.getContext().getColor(resourceId));
    }

    @BindingAdapter("app:card_suit_dimen")
    public static void setSuitDimensions(ImageView view, boolean onPokerTable) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (onPokerTable) {
            layoutParams.height = (int) view.getResources().getDimension(R.dimen.card_height_small);
            layoutParams.width = (int) view.getResources().getDimension(R.dimen.card_width_small);
        } else {
            layoutParams.height = (int) view.getResources().getDimension(R.dimen.card_height);
            layoutParams.width = (int) view.getResources().getDimension(R.dimen.card_width);
        }
        view.setLayoutParams(layoutParams);
    }

    /**
     * Update visibility binding to support booleans
     */
    @BindingAdapter("android:visibility")
    public static void setVisibilityByBoolean(View view, Boolean bool) {
        view.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    /**
     * Update textview so they can be binded with various literals.
     */
    @BindingAdapter("android:text")
    public static void setInt(TextView view, int input) {
        view.setText(String.valueOf(input));
    }

    @BindingAdapter("android:text")
    public static void setChar(TextView view, char input) {
        view.setText(String.valueOf(input));
    }

    @BindingAdapter("android:text")
    public static void setDouble(TextView view, double input) {
        view.setText(String.valueOf(input));
    }

    @BindingAdapter("app:start")
    public static void start(CircularProgressBar view, boolean start) {
        if (start) view.start();
        else view.cancel();
    }

}