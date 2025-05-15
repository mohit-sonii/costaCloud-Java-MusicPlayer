package appolo.project.Controllers;

import appolo.project.Entity.Playlist;
import appolo.project.Entity.User;
import appolo.project.Service.PlaylistService;
import appolo.project.Service.UserService;
import appolo.project.Util.TokenCookie;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// Although Spring Security will automatticalyy handle the authentication for whether token is valid or not whether user exists or not. But still just for safety I intriduce validation in each route. It can be improved as well but as of know I focus on to make it happen. Authenticaion check in each route is redudant but for security I think its good practice.

@RestController
@RequestMapping("/u/playlist")
public class PlaylistController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenCookie tokenCookie;

    @Autowired
    private PlaylistService playlistService;

    @PostMapping("/add")
    public ResponseEntity<String> addSongInPlaylist(
            @RequestParam("playlistName") String playlist_name,
            @RequestParam("songId") String song_id,
            @CookieValue(value = "auth_for_sec", defaultValue = "") String cookie
    ) {
        try {
            String[] result = tokenCookie.tokenValidation(cookie, "USER");
            if (!result[0].equals("true") || !result[1].equals("USER")) {
                return new ResponseEntity<>("UnAuthorized", HttpStatus.UNAUTHORIZED);
            }
            Claims claims = tokenCookie.getClaims(cookie, "USER");
            String username = claims.getSubject();
            User foundUser = userService.findTheUser(username).orElse(null);
            if (foundUser == null) {
                return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
            }
            return playlistService.addInList(foundUser, playlist_name, song_id);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSongFromPlaylist(
            @RequestParam("playlistName") String playlist_name,
            @RequestParam("songId") String song_id,
            @CookieValue(value = "auth_for_sec", defaultValue = "") String cookie
    ) {
        try {
            String[] result = tokenCookie.tokenValidation(cookie, "USER");
            if (!result[0].equals("true") || !result[1].equals("USER")) {
                return new ResponseEntity<>("UnAuthorized", HttpStatus.UNAUTHORIZED);
            }
            Claims claims = tokenCookie.getClaims(cookie, "USER");
            String username = claims.getSubject();
            User foundUser = userService.findTheUser(username).orElse(null);
            if (foundUser == null) {
                return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
            }
            return playlistService.deleteFromList(foundUser,playlist_name,song_id);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Erorr" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<Playlist>> getUserPlaylist(@CookieValue(value="auth_for_sec",defaultValue = "")String cookie){
        try {
            String[] result = tokenCookie.tokenValidation(cookie, "USER");
            if (!result[0].equals("true") || !result[1].equals("USER")) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
            Claims claims = tokenCookie.getClaims(cookie, "USER");
            String username = claims.getSubject();
            User foundUser = userService.findTheUser(username).orElse(null);
            if (foundUser == null) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND);
            }
            return playlistService.getPlaylist(foundUser);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}