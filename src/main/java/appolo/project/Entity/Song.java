package appolo.project.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Entity
@Table(name="song")
@Getter
@Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID song_id;

    @Column(name = "genre")
    private String genre;

    @Column(name = "artist")
    private String artist;

    @Column(name="title")
    private String title;

    @ManyToMany(mappedBy = "songs")
    @JsonBackReference
    private List<Playlist> playlists;
}
