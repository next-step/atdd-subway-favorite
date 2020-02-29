package atdd.favorite.domain;

import atdd.favorite.application.dto.FavoriteStationRequestView;

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

    public FavoriteStation(String email, Long stationId) {
        this(0L, email, stationId);
    }

    public FavoriteStation(Long id, String email, Long stationId) {
        this.id = id;
        this.email = email;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getStationId() {
        return stationId;
    }

    public static FavoriteStation of(FavoriteStationRequestView requestView) {
        return new FavoriteStation(requestView.getEmail(), requestView.getStationId());
    }
}
