package nextstep.favorite.dto.response;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.station.dto.response.StationResponse;

@Builder
@Getter
public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

}
