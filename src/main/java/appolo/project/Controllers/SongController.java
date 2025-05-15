package appolo.project.Controllers;

import appolo.project.Entity.Song;
import appolo.project.Service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @GetMapping("/search")
    public ResponseEntity<List<Song>> SearchSongs(@RequestParam(name = "genre",required = false) String genre, @RequestParam(name="title",required = false)String title, @RequestParam(name="artist",required = false)String artist){

        List<Song> result = songService.searchOperations(genre,title,artist);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // get all the songs
    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs(){
        List<Song> result  =songService.getSongs();
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
