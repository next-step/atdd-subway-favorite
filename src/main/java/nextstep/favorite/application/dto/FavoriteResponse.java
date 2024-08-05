package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.application.dto.StationResponse;

@Getter
@AllArgsConstructor
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
            favorite.getId(),
            StationResponse.from(favorite.getSourceStation()),
            StationResponse.from(favorite.getTargetStation())
        );
    }
}
