package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

import java.util.List;

public class FavoritePathResponse {
    private List< FavoriteResponse > favorite;

    public FavoritePathResponse() {}

    public FavoritePathResponse(List< FavoriteResponse > favorite) {
        this.favorite = favorite;
    }

    public static FavoritePathResponse of(List< FavoriteResponse > favorite){
       return new FavoritePathResponse(favorite);
    }

    public List< FavoriteResponse > getFavorite() {
        return favorite;
    }
}
