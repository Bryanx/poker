package be.kdg.mobile_client.shared;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import androidx.databinding.BindingAdapter;
import be.kdg.mobile_client.R;

/**
 * Extends several databinding keywords in layout files.
 */
public class DataBindingAdapters {
    private static final String FOLD = "FOLD";
    private static final String CALL = "CALL";
    private static final String CHECK = "CHECK";
    private static final String RAISE = "RAISE";
    private static final String BET = "BET";
    private static final String UNDECIDED = "UNDECIDED";
    private static final String SPADES = "SPADES";
    private static final String CLUBS = "CLUBS";
    private static final String TWO = "two";
    private static final String THREE = "three";
    private static final String FOUR = "four";
    private static final String FIVE = "five";
    private static final String SIX = "six";
    private static final String SEVEN = "seven";
    private static final String EIGHT = "eight";
    private static final String NINE = "nine";
    private static final String TEN = "ten";
    private static final String JACK = "jack";
    private static final String QUEEN = "queen";
    private static final String KING = "king";
    private static final String ACE = "ace";
    private static final String CARD_SYMBOL_ = "card_symbol_";
    private static final String SMALL = "_small";
    private static final String DRAWABLE = "drawable";

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
            case TWO: rank = "2"; break;
            case THREE: rank = "3"; break;
            case FOUR: rank = "4"; break;
            case FIVE: rank = "5"; break;
            case SIX: rank = "6"; break;
            case SEVEN: rank = "7"; break;
            case EIGHT: rank = "8"; break;
            case NINE: rank = "9"; break;
            case TEN: rank = "10"; break;
            case JACK: rank = "J"; break;
            case QUEEN: rank = "Q"; break;
            case KING: rank = "K"; break;
            case ACE: rank = "A"; break;
        }
        view.setText(rank);
    }

    @BindingAdapter("app:card_suit")
    public static void setCardSuit(ImageView view, String cardType) {
        if (cardType == null) return;
        StringBuilder type = new StringBuilder(CARD_SYMBOL_);
        type.append(cardType.split("_")[2].toLowerCase());
        int resourceId = view.getResources().getIdentifier(type.append(SMALL).toString(), DRAWABLE,
                view.getContext().getPackageName());
        view.setImageResource(resourceId);
    }

    @BindingAdapter("app:card_text_color")
    public static void setCardColor(TextView view, String cardType) {
        if (cardType == null || cardType.contains(SPADES) || cardType.contains(CLUBS)) {
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
            case FOLD: resourceId = R.color.colorRed;break;
            case CALL: resourceId = R.color.colorGreen;break;
            case CHECK: resourceId = R.color.colorGreen;break;
            case RAISE: resourceId = R.color.colorYellow;break;
            case BET: resourceId = R.color.colorYellow;break;
            case UNDECIDED: resourceId = R.color.colorTransparent;
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
    public static void startCircularProgressBar(CircularProgressBar view, boolean start) {
        if (start) view.start();
        else view.cancel();
    }

}