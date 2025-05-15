package appolo.project.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="playback")
@Getter
@Setter
public class PlayBack {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID playback_id;

    @OneToOne
    private User user;

    @ManyToOne
    private Song currentSong;

    @Enumerated(EnumType.STRING)
    private PlaySong songStatus;


}
