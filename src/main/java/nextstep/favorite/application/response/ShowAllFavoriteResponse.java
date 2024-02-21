package nextstep.favorite.application.response;

import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.application.dto.CreateLineSectionDto;

import java.util.List;
import java.util.stream.Collectors;

public class ShowAllFavoriteResponse {

    private List<FavoriteDto> favorites;

    private ShowAllFavoriteResponse() {
    }

    private ShowAllFavoriteResponse(List<FavoriteDto> favorites) {
        this.favorites = favorites;
    }

    public static ShowAllFavoriteResponse from(List<Favorite> favorites) {
        return new ShowAllFavoriteResponse(
                favorites.stream()
                        .map(FavoriteDto::from)
                        .collect(Collectors.toList())
        );
    }

    public List<FavoriteDto> getFavorites() {
        return favorites;
    }

}
