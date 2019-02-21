package be.kdg.userservice.user.persistence;

import be.kdg.userservice.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    @Query("select userrole.role from UserRole userrole, User user where user.username=?1 and userrole.userId=user.id")
    List<String> findRoleByUserName(String username);
    UserRole findByUserId(String userId);
}
