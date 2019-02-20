package be.kdg.mobile_client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Register {
    private String username;
    private String email;
    private String password;
}
