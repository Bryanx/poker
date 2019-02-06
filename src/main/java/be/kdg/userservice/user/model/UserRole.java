package be.kdg.userservice.user.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Data
@Entity
@Table(name = "tb_user_role")
public class UserRole {
    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    private String id;
    private String userId;
    private String role;

    public UserRole(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }
}
