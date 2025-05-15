package appolo.project.Controllers;

import appolo.project.Entity.User;
import appolo.project.Repository.UserRepo;
import appolo.project.Service.PlaybackService;
import appolo.project.Util.TokenCookie;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/u/song/listen")
public class PlaybackController {
    @Autowired
    private PlaybackService playbackService;

    @Autowired
    private TokenCookie tokenCookie;

    @Autowired
    private UserRepo userRepo;

    private User currentUser = null;

    private void assignUser(String token) {
        if (currentUser == null) {
            Claims claims = tokenCookie.getClaims(token, "USER");
            String getUserId = claims.get("id", String.class);
            UUID uuid_user = UUID.fromString(getUserId);
            this.currentUser = userRepo.findById(uuid_user).orElse(null);
        }
    }

    @PostMapping("/play")
    public ResponseEntity<String> playSong(@RequestParam(value = "songId") String song_id, @CookieValue(value = "auth_for_sec", defaultValue = "") String cookie) {
        try {
            assignUser(cookie);
            return playbackService.runTheSong(currentUser, song_id);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Erorr " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseSong(@RequestParam(value = "songId") String song_id, @CookieValue(value = "auth_for_sec", defaultValue = "") String cookie) {
        try {
            assignUser(cookie);
            return playbackService.pauseStopResume(currentUser, "PAUSE");
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Erorr " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopTheSong(@RequestParam(value = "songId") String song_id, @CookieValue(value = "auth_for_sec", defaultValue = "") String cookie) {
        try {
            assignUser(cookie);
            return playbackService.pauseStopResume(currentUser, "STOP");
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Erorr " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resumeThesong(@RequestParam(value = "songId") String song_id, @CookieValue(value = "auth_for_sec", defaultValue = "") String cookie) {
        try {
            assignUser(cookie);
            return playbackService.pauseStopResume(currentUser, "RESUME");
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Erorr " + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, String>> findCurrentSong(@CookieValue(value = "auth_for_sec", defaultValue = "") String cookie) {
        assignUser(cookie);
        return playbackService.findCurrent(currentUser);
    }

}
