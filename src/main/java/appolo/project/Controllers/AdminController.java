package appolo.project.Controllers;

import appolo.project.Entity.Song;
import appolo.project.Service.SongService;
import appolo.project.Util.TokenCookie;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/a/song")
public class AdminController {

    @Autowired
    private TokenCookie tokenCookie;

    @Autowired
    private SongService songService;

    @PostMapping("/add")
    public ResponseEntity<String> addSongs(@RequestBody Song song, @CookieValue(value = "auth_for_sec",defaultValue = "")String cookie){
        if(cookie.isEmpty()){
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        String[] result = tokenCookie.tokenValidation(cookie,"ADMIN");
        if(!result[0].equals("true") || !result[1].equals("ADMIN")){
            return new ResponseEntity<>("User Unauthorized",HttpStatus.FORBIDDEN);
        }
        Claims claims = tokenCookie.getClaims(cookie,"ADMIN");
        if(!claims.get("role",String.class).equals("ADMIN")){
            return new ResponseEntity<>("User Unauthorized",HttpStatus.FORBIDDEN);
        }
        return songService.saveSong(song);
    }
}
