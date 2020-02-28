package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;
import lombok.Builder;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class CreateFavoriteStationRequestView {
    private Long stationId;
    private String email;

    public CreateFavoriteStationRequestView() {
    }

    @Builder
    public CreateFavoriteStationRequestView(String email, Long stationId) {
        this.stationId = stationId;
        this.email = email;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getEmail() {
        return email;
    }

    public FavoriteStation toEntity(CreateFavoriteStationRequestView requestView){
        return FavoriteStation.of(requestView);
    }
}
