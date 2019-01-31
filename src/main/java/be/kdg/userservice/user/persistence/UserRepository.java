package be.kdg.userservice.user.persistence;

import be.kdg.userservice.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserName(String username);
}
