package nextstep.favorite.service.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.service.dto.StationResponse;

@Getter
@Builder
public class FavoritePathResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;
}
