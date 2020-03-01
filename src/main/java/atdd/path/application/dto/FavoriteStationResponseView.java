package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;

import java.io.Serializable;

public class FavoriteStationResponseView implements Serializable {
    private static final long serialVersionUID = 7427780022096947903L;

    private Long id;
    private UserResponseView user;
    private StationResponseView station;

    public FavoriteStationResponseView() {
    }

    public FavoriteStationResponseView(FavoriteStation favoriteStation) {
        this.id = favoriteStation.getId();
        this.user = UserResponseView.of(favoriteStation.getUser());
        this.station = StationResponseView.of(favoriteStation.getStation());
    }

    public static FavoriteStationResponseView of(FavoriteStation favoriteStation) {
        return new FavoriteStationResponseView(favoriteStation);
    }

    public Long getId() {
        return id;
    }

    public UserResponseView getUser() {
        return user;
    }

    public StationResponseView getStation() {
        return station;
    }
}
