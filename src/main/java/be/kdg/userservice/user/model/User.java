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
    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private String email;
    private int enabled;
    private byte[] profilePictureBinary;
    private String socialId;
    private String profilePictureSocial;
    private String provider;
}
