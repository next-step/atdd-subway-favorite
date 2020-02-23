package atdd.favorite.application.dto;

import atdd.favorite.domain.FavoriteStation;

public class FavoriteStationResponseView {
    private Long id;
    private String userEmail;
    private Long favoriteStationId;

    public Long getFavoriteStationId() {
        return favoriteStationId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationResponseView() {
    }

    public FavoriteStationResponseView(Long id, String userEmail, Long favoriteStationId) {
        this.id = id;
        this.userEmail = userEmail;
        this.favoriteStationId = favoriteStationId;
    }

    public static FavoriteStationResponseView of(FavoriteStation createdFavoriteStation) {
        return new FavoriteStationResponseView(
                createdFavoriteStation.getId(),
                createdFavoriteStation.getUserEmail(),
                createdFavoriteStation.getStationId()
        );
    }
}
