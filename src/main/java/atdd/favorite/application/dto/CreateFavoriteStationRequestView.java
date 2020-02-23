package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class CreateFavoriteStationRequestView {
    private Long id;
    private String userEmail;
    private Long stationId;

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getStationId() {
        return stationId;
    }

    public CreateFavoriteStationRequestView() {
    }

    public CreateFavoriteStationRequestView(Long stationId) {
        this.stationId = stationId;
    }

    public void insertUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public FavoriteStation toEntity() {
        return new FavoriteStation(userEmail, stationId);
    }
}
