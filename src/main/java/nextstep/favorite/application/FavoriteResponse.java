package nextstep.favorite.application;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;
import nextstep.subway.application.StationResponse;

@Builder
@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorites, Station sourceStation, Station targetStation) {
        return FavoriteResponse.builder()
                .id(favorites.getId())
                .source(StationResponse.of(sourceStation))
                .target(StationResponse.of(targetStation))
                .build();
    }
}
