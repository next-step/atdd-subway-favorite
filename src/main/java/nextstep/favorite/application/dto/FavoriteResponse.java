package nextstep.favorite.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.station.StationResponse;

@Getter
@Builder
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse from(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .source(StationResponse.from(favorite.getSourceStation()))
                .target(StationResponse.from(favorite.getTargetStation()))
                .build();
    }
}
