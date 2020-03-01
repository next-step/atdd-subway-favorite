package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;

public class FavoriteStationResponseView {
    private Long id;
    private String email;
    private Long stationId;

    public FavoriteStationResponseView() {
    }

    public FavoriteStationResponseView(Long id, String email, Long stationId) {
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

    public static FavoriteStationResponseView of(FavoriteStation favoriteStation) {
        return new FavoriteStationResponseView(
                favoriteStation.getId(),
                favoriteStation.getEmail(),
                favoriteStation.getStationId());
    }
}
