package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;
import lombok.Builder;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class FavoriteStationRequestView {
    private String email;
    private Long stationId;

    public FavoriteStationRequestView() {
    }

    @Builder
    public FavoriteStationRequestView(String email, Long stationId) {
        this.email = email;
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getEmail() {
        return email;
    }

    public void insertEmail(String email) {
        this.email = email;
    }

    public FavoriteStation toEntity(FavoriteStationRequestView requestView) {
        return FavoriteStation.of(requestView);
    }

    public static FavoriteStationRequestView of(FavoriteStation favoriteStation) {
        return new FavoriteStationRequestView(favoriteStation.getEmail(),
                favoriteStation.getStationId());
    }
}
