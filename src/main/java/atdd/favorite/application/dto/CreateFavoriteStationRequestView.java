package atdd.favorite.application.dto;

public class CreateFavoriteStationRequestView {
    private Long stationId;

    public CreateFavoriteStationRequestView() {
    }

    public CreateFavoriteStationRequestView(Long stationId) {
        this.stationId = stationId;
    }
}
