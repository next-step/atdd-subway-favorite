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

    private String userEmail;
    private Long stationId;

    public FavoriteStation() {
    }

    public FavoriteStation(String userEmail, Long stationId) {
        this.userEmail = userEmail;
        this.stationId = stationId;
    }

    public FavoriteStation(Long id, String userEmail, Long stationId) {
        this.id = id;
        this.userEmail = userEmail;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getStationId() {
        return stationId;
    }

    public void insertStationId(Long stationId) {
        this.stationId = stationId;
    }
}
