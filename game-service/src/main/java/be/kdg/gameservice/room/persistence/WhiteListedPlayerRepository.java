package be.kdg.gameservice.room.persistence;

import be.kdg.gameservice.room.model.WhiteListedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteListedPlayerRepository extends JpaRepository<WhiteListedUser, Integer> {
}
