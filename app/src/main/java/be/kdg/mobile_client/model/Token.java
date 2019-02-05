package be.kdg.mobile_client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Token {
    @SerializedName("access_token")
    @Expose
    private String access_token;

    @SerializedName("token_type")
    @Expose
    private String token_type;

    private String refresh_token;
    private String expires_in;
    private String scope;
    private String role;
    private String uuid;
    private String username;
    private String jti;
}
