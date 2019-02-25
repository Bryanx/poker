package be.kdg.gameservice.room.persistence;

import be.kdg.gameservice.room.model.WhiteListedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListedPlayerRepository extends JpaRepository<WhiteListedPlayer, Integer> {
}
