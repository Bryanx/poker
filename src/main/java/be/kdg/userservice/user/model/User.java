package be.kdg.userservice.user.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @GeneratedValue(generator = "uuid-gen")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    private String id;

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private int enabled;
}
