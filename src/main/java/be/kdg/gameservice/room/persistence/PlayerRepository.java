package be.kdg.gameservice.room.persistence;

import be.kdg.gameservice.room.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player getByUserId(String userId);
}
