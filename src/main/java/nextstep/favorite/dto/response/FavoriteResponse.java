package nextstep.favorite.dto.response;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.entity.Favorite;
import nextstep.subway.station.dto.response.StationResponse;

@Builder
@Getter
public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .source(StationResponse.of(favorite.getSource()))
                .target(StationResponse.of(favorite.getTarget()))
                .build();
    }

}
