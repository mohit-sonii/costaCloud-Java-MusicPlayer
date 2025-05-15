package appolo.project.Service;

import appolo.project.Entity.Playlist;
import appolo.project.Entity.Song;
import appolo.project.Entity.User;
import appolo.project.Repository.PlaylistRepo;
import appolo.project.Repository.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepo playlistRepo;

    @Autowired
    private SongRepo songRepo;

    public ResponseEntity<String> addInList(User user, String playlistName, String song_id) {

        // first try to find the playlist with user and palylist name in playlist table.
        Playlist playlist = playlistRepo.findByUserAndPlaylistName(user, playlistName)
                .orElseGet(() -> {
                    Playlist p = new Playlist();
                    p.setPlaylistName(playlistName);
                    p.setUser(user);
                    return playlistRepo.save(p);
                });
        // conver tthe string into UUId
        UUID song_id_uuid = UUID.fromString(song_id);
        // find the song we want to add

        Song song = songRepo.findById(song_id_uuid).orElse(null);
        if (song == null) {
            return new ResponseEntity<>("Song does nto exists", HttpStatus.NOT_FOUND);
        }

        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            playlistRepo.save(playlist);
            return new ResponseEntity<>("Playlist Updated", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Song Already in Playlist", HttpStatus.NOT_MODIFIED);
    }
}
