package appolo.project.Service;

import appolo.project.Entity.Song;
import appolo.project.Repository.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepo songRepo;

    public ResponseEntity<String> saveSong(Song song){
        try{
            songRepo.save(song);
            return new ResponseEntity<>("Song added Successfully",HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Error while Adding a song", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Song> searchOperations(String genre,String title,String artist){
        return songRepo.perfectMatcher(genre,title,artist);
    }

    public List<Song> getSongs(){
        return songRepo.getAll();
    }
}
