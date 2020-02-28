package atdd.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FavoriteStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Long stationId;

    public FavoriteStation() {
    }

    public FavoriteStation(Long id, String email, Long stationId) {
        this.id = id;
        this.email = email;
        this.stationId = stationId;
    }
}
