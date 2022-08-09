package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Favorite;

@Builder
@Getter
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse from(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.id())
                .source(StationResponse.of(favorite.sourceStation()))
                .target(StationResponse.of(favorite.targetStation()))
                .build();
    }
}
