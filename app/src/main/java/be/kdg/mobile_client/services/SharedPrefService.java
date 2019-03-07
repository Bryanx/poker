package be.kdg.mobile_client.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import be.kdg.mobile_client.R;
import be.kdg.mobile_client.model.JWTBody;
import be.kdg.mobile_client.model.Token;
import be.kdg.mobile_client.shared.JWTUtils;

/**
 * Service for shared preferences (local storage)
 */
public class SharedPrefService {

    /**
     * Writes token to shared preferences for later use.
     */
    public void saveToken(Context ctx, Token token) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String tokenJson = new Gson().toJson(token);
        editor.putString(ctx.getString(R.string.token), tokenJson);
        editor.apply();
    }

    /**
     * Checks if there is a token in shared preferences.
     */
    public boolean hasToken(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String json = sharedPref.getString("token", "");
        if (json == null || json.equals("null") || json.isEmpty()) {
            return false;
        }
        Token token = new Gson().fromJson(json, Token.class);
        return token.isSignedIn();
    }

    /**
     * Reads token from shared preferences.
     */
    public Token getToken(Context ctx) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(ctx.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String json = sharedPref.getString("token", "");
        if (json == null || json.equals("null") || json.isEmpty()) {
            return null;
        }
        return new Gson().fromJson(json, Token.class);
    }

    public String getUserId(Context ctx) {
        Token token = this.getToken(ctx);
        String tokenBody = "";
        try {
            tokenBody = JWTUtils.decodeJWTBody(token.getAccessToken());
        } catch (Exception e) {
            Log.e("Can't decode JWTBody", "Unable to decode body of JWTBody used for user");
        }

        Gson gson = new GsonBuilder().create();
        JWTBody jwtBody = gson.fromJson(tokenBody, JWTBody.class);
        return jwtBody.getUuid();
    }

}
