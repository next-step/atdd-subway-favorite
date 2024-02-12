package nextstep.favorite.application.dto;

import lombok.Getter;
import nextstep.station.StationResponse;

@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;
}
