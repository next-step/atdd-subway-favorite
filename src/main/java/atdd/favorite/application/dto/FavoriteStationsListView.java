package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;

import java.util.List;

public class FavoriteStationsListView {
    private String userEmail;
    private List<FavoriteStation> favoriteStations;

    public FavoriteStationsListView(String userEmail, List<FavoriteStation> favoriteStations) {
        this.userEmail = userEmail;
        this.favoriteStations = favoriteStations;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<FavoriteStation> getFavoriteStations() {
        return favoriteStations;
    }
}
