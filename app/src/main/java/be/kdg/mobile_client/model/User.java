package be.kdg.mobile_client.model;

import lombok.Data;

@Data
public class User {
    private String id;
    private String username;
    private String email;
    private String password;
}
