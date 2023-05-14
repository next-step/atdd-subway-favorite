package nextstep.favorites.application.dto;

import lombok.Getter;
import nextstep.favorites.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;


@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), new StationResponse(favorite.getSource().getId(), favorite.getSource().getName()), new StationResponse(favorite.getTarget().getId(), favorite.getTarget().getName()));
    }
}
