package be.kdg.mobile_client.shared;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import be.kdg.mobile_client.model.ActType;

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

}