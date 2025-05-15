package appolo.project.Service;

import appolo.project.Entity.PlayBack;
import appolo.project.Entity.PlaySong;
import appolo.project.Entity.Song;
import appolo.project.Entity.User;
import appolo.project.Repository.PlayBackRepo;
import appolo.project.Repository.SongRepo;
import appolo.project.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlaybackService {
    @Autowired
    private PlayBackRepo playBackRepo;


    @Autowired
    private SongRepo songRepo;

    // run the song
    public ResponseEntity<String> runTheSong(User user, String song_id) {
        UUID song_id_uuid = UUID.fromString(song_id);

        Song song_result = songRepo.findById(song_id_uuid).orElse(null);
        if (song_result == null) {
            return new ResponseEntity<>("Song not Exists", HttpStatus.NOT_FOUND);
        }

        PlayBack playBackResult = playBackRepo.findByUser(user).orElse(new PlayBack());

        playBackResult.setCurrentSong(song_result);
        playBackResult.setUser(user);
        playBackResult.setSongStatus(PlaySong.PLAYING);

        playBackRepo.save(playBackResult);
        return new ResponseEntity<>("Song is playing", HttpStatus.OK);

    }

    // run/ pause and stp the song
    public ResponseEntity<String> pauseStopResume(User user, String type) {

        Optional<PlayBack> playBackResult = playBackRepo.findByUser(user);
        if (playBackResult.isEmpty()) {
            return new ResponseEntity<>("PlackBack is not available", HttpStatus.NOT_FOUND);
        }
        PlayBack actualData = playBackResult.get();
        switch (type) {
            case "PAUSE" -> {
                if (actualData.getSongStatus() == PlaySong.PLAYING) {
                    actualData.setSongStatus(PlaySong.PAUSE);
                    playBackRepo.save(actualData);
                }
                return new ResponseEntity<>("Song is Paused", HttpStatus.OK);
            }
            case "STOP" -> {
                actualData.setSongStatus(PlaySong.STOPPED);
                playBackRepo.save(actualData);
                return new ResponseEntity<>("Song is Stopped", HttpStatus.OK);
            }
            case "RESUME" -> {
                if (actualData.getSongStatus() == PlaySong.STOPPED)
                    actualData.setSongStatus(PlaySong.PLAYING);
                playBackRepo.save(actualData);
                return new ResponseEntity<>("Song is Playing", HttpStatus.OK);
            }
        }
       return new ResponseEntity<>("Invalid Request",HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Map<String,String>> findCurrent(User user){
        PlayBack playBack = playBackRepo.findByUser(user).orElse(null);
        if(playBack==null)return new ResponseEntity<>(new HashMap<>(),HttpStatus.NO_CONTENT);
        Map<String,String> map = new HashMap<>();
        map.put("song",playBack.getCurrentSong().getTitle());
        map.put("status",playBack.getSongStatus().toString());
        return new ResponseEntity<>(map,HttpStatus.OK);

    }

}
