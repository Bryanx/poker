package be.kdg.mobile_client.model;

import lombok.Data;

@Data
public class JWTBody {
    String role;
    String user_name;
    String uuid;
}
