package be.kdg.userservice.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Simple pojo class that is used to hold a friendship with another user.
 */
@Entity
@Table(name = "tb_friend")
@Getter
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userId;

    public Friend(String userId) {
        this.userId = userId;
    }
}
