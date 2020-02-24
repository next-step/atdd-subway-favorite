package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;

public class FavoriteStationResponseView {

    private FavoriteStation favoriteStation;

    private FavoriteStationResponseView() {}

    public FavoriteStationResponseView(FavoriteStation favoriteStation) {
        this.favoriteStation = favoriteStation;
    }

    public Long getId() {
        return favoriteStation.getId();
    }

    public StationResponseView getStation() {
        return StationResponseView.of(favoriteStation.getStation());
    }

}
