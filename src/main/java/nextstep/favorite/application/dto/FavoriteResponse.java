package nextstep.favorite.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.station.application.dto.StationResponse;
import nextstep.station.domain.Station;


@Builder
@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorites, Station sourceStation, Station targetStation) {
        return FavoriteResponse.builder()
                .id(favorites.getId())
                .source(StationResponse.createResponse(sourceStation))
                .target(StationResponse.createResponse(targetStation))
                .build();
    }
}
