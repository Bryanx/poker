package be.kdg.mobile_client.shared;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

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
}