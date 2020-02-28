package atdd.favorite.application.dto;

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
}
