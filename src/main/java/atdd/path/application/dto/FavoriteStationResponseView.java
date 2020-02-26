package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;

public class FavoriteStationResponseView {

    private Long id;
    private StationResponseView station;

    private FavoriteStationResponseView() {}

    public FavoriteStationResponseView(FavoriteStation favoriteStation) {
        this.id = favoriteStation.getId();
        this.station = StationResponseView.of(favoriteStation.getStation());
    }

    public Long getId() {
        return id;
    }

    public StationResponseView getStation() {
        return station;
    }

}
