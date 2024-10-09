package nextstep.favorite.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.StationResponse;

@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    @Builder
    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite, StationResponse sourceResponse,
                                      StationResponse targetResponse) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .source(sourceResponse)
                .target(targetResponse)
                .build();
    }
}
