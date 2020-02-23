package atdd.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FavoritePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private Long startStationId;
    private Long endStationId;

    public FavoritePath() {
    }

    public FavoritePath(String userEmail, Long startStationId, Long endStationId) {
        this.userEmail = userEmail;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
