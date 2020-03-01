package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;

public class FavoriteStationRequestView {
    private Long id;
    private String email;
    private Long stationId;

    public FavoriteStationRequestView() {
    }

    public FavoriteStationRequestView(Long stationId) {
        this(0L, "", stationId);
    }

    public FavoriteStationRequestView(String email, Long stationId) {
        this(0L, "", stationId);
    }

    public FavoriteStationRequestView(Long id, String email, Long stationId) {
        this.id = id;
        this.email = email;
        this.stationId = stationId;
    }

    public Long getId() {
        return id;
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

    public void insertId(Long id) {
        this.id = id;
    }

    public FavoriteStation toEntity(FavoriteStationRequestView requestView) {
        return FavoriteStation.of(requestView);
    }

    public static FavoriteStationRequestView of(FavoriteStation favoriteStation) {
        return new FavoriteStationRequestView(0L, favoriteStation.getEmail(),
                favoriteStation.getStationId());
    }
}
