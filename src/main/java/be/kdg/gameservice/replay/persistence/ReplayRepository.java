package be.kdg.gameservice.replay.persistence;

import be.kdg.gameservice.replay.model.Replay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplayRepository extends JpaRepository<Replay, Integer> {
    List<Replay> getAllByOwnerId(String ownerId);
}
