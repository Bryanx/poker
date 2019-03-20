package be.kdg.mobile_client.shared;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * JWTUtils is responsible for decoding the JWTBody
 */
class JWTUtils {

    private static final String JWT_DECODED = "JWT_DECODED";
    private static final String HEADER = "Header: ";
    private static final String BODY = "Body: ";

    static String decodeJWTBody(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d(JWT_DECODED, HEADER + getJson(split[0]));
            Log.d(JWT_DECODED, BODY + getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
        return getJson(JWTEncoded.split("\\.")[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}