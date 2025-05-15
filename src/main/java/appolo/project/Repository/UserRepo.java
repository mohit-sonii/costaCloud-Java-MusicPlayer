package appolo.project.Repository;

import appolo.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    // here i write custom query
    @Query("Select u from User u where u.username=:username")
    Optional<User> findByUsername(@Param("username")String username);
}
