package be.kdg.userservice.notification.persistence;

import be.kdg.userservice.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
