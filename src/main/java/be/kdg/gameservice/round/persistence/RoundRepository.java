package be.kdg.gameservice.round.persistence;

import be.kdg.gameservice.round.model.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Integer> {
}
