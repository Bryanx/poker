package be.kdg.mobile_client.shared;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Pattern;

/**
 * An Email format validator for {@link android.widget.EditText}.
 */
public class EmailValidator implements TextWatcher {

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private boolean isValid = false;

    public boolean isValid() {
        return isValid;
    }

    /**
     * Validates if the given input is a valid email address.
     */
    public static boolean isValidEmail(CharSequence email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    final public void afterTextChanged(Editable editableText) {
        isValid = isValidEmail(editableText);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {}
}