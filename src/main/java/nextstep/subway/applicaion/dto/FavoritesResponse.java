package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

import java.util.List;

public class FavoritesResponse {
    private List<FavoriteResponse> favorites;

    public FavoritesResponse(List<FavoriteResponse> favorites) {
        this.favorites = favorites;
    }

    public List<FavoriteResponse> getFavorites() {
        return favorites;
    }
}
