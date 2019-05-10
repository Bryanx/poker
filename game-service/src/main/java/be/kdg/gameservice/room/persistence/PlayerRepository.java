package be.kdg.gameservice.room.persistence;

import be.kdg.gameservice.room.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Optional<Player> getByUserId(String userId);
}
