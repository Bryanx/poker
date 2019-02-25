package be.kdg.mobile_client.shared;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Pattern;

/**
 * A username format validator for {@link android.widget.EditText}.
 */
public class UsernameValidator implements TextWatcher {

    public static final Pattern USERNAME_PATTERN = Pattern.compile("^([a-z]|[A-Z]|[0-9]){4,255}$");

    private boolean isValid = false;

    public boolean isValid() {
        return isValid;
    }

    /**
     * Validates if the given input is a valid username.
     * System username is not allowed.
     */
    public static boolean isValidUsername(CharSequence username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches() && username != "system";
    }

    @Override
    final public void afterTextChanged(Editable editableText) {
        isValid = isValidUsername(editableText);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {}
}