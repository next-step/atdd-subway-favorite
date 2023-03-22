package nextstep.subway.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

@Getter
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    @Builder
    public FavoriteResponse(Long id, StationResponse sourceResponse, StationResponse targetResponse) {
        this.id = id;
        this.source = sourceResponse;
        this.target = targetResponse;
    }

    public static FavoriteResponse of(Favorite favorite) {

        Station source = favorite.getSource();
        Station target = favorite.getTarget();

        return FavoriteResponse.builder()
            .id(favorite.getId())
            .sourceResponse(StationResponse.of(source))
            .targetResponse(StationResponse.of(target))
            .build();
    }
}
