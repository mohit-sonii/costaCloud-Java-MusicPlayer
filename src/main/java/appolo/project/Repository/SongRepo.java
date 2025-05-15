package appolo.project.Repository;

import appolo.project.Entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SongRepo extends JpaRepository<Song, UUID> {
    @Query("SELECT s from Song s where (:genre is null or genre=:genre) and (:artist is null or artist=:artist) and (:title is null or title=:title)")
    List<Song> perfectMatcher(@Param("genre") String genre, @Param("title") String title, @Param("artist") String artist);

    @Query("SELECT s from Song s")
    List<Song> getAll();
}
