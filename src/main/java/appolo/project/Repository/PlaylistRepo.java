package appolo.project.Repository;

import appolo.project.Entity.Playlist;
import appolo.project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlaylistRepo extends JpaRepository<Playlist, UUID> {
    Optional<Playlist> findByUserAndPlaylistName(User user, String playlistName);

}
