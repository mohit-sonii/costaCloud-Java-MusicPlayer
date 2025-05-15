package appolo.project.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="playlist")
@Getter
@Setter
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID playlist_id;

    @Column(name = "playlist_name")
    private String playlistName;

    // each playlist belong to one user;
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private User user;

    // each playlist can have many songs and they can be in many playlist
    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name="song_id")
    )
    @JsonManagedReference
    private List<Song> songs;
}
