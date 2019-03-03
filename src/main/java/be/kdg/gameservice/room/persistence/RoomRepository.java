package be.kdg.gameservice.room.persistence;

import be.kdg.gameservice.room.model.PrivateRoom;
import be.kdg.gameservice.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    Room getRoomByName(String name);
}
