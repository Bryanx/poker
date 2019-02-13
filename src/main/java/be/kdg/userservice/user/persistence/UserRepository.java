package be.kdg.userservice.user.persistence;

import be.kdg.userservice.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    List<User> findAllByUsername(String username);
    Optional<User> findBySocialId(String socialId);
}
