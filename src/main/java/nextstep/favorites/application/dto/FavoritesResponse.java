package nextstep.favorites.application.dto;

import lombok.Getter;
import nextstep.favorites.domain.Favorites;
import nextstep.subway.applicaion.dto.StationResponse;


@Getter
public class FavoritesResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoritesResponse() {
    }

    public FavoritesResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoritesResponse of(Favorites favorites) {
        return new FavoritesResponse(favorites.getId(), new StationResponse(favorites.getSource().getId(), favorites.getSource().getName()), new StationResponse(favorites.getTarget().getId(), favorites.getTarget().getName()));
    }
}
