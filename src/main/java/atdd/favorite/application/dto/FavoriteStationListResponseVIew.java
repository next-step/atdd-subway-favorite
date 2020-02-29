package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;

import java.util.List;

public class FavoriteStationListResponseVIew {
    private String email;
    private List<FavoriteStation> favoriteStations;

    public FavoriteStationListResponseVIew() {
    }

    public FavoriteStationListResponseVIew(String email, List<FavoriteStation> favoriteStations) {
        this.email = email;
        this.favoriteStations = favoriteStations;
    }

    public String getEmail() {
        return email;
    }

    public List<FavoriteStation> getFavoriteStations() {
        return favoriteStations;
    }

    public static FavoriteStationListResponseVIew of(String email, List<FavoriteStation> favoriteStations) {
        return new FavoriteStationListResponseVIew(email, favoriteStations);
    }
}
