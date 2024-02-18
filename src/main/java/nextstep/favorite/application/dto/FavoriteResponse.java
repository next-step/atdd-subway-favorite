package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Favorite favorite) {
        id = favorite.getId();
        source = new StationResponse(favorite.getSourceStation());
        target = new StationResponse(favorite.getTargetStation());
    }
}
