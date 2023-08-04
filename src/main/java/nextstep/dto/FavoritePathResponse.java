package nextstep.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.FavoritePath;
import nextstep.domain.Station;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FavoritePathResponse {

    private Long id;
    private Station source;
    private Station target;

    public static FavoritePathResponse createFavoritePathResponse(FavoritePath favoritePath){
        FavoritePathResponse favoritePathResponse = new FavoritePathResponse();
        favoritePathResponse.source = favoritePath.getSource();
        favoritePathResponse.target = favoritePath.getTarget();

        return favoritePathResponse;
    }
}
