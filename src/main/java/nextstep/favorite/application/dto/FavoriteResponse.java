package nextstep.favorite.application.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.controller.dto.StationResponse;


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
}
