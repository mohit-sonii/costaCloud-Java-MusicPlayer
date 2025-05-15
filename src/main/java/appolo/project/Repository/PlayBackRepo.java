package appolo.project.Repository;

import appolo.project.Entity.PlayBack;
import appolo.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayBackRepo extends JpaRepository<PlayBack, UUID> {

     Optional<PlayBack> findByUser(User user);
}
