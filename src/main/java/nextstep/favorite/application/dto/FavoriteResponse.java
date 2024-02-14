package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nextstep.subway.controller.dto.StationResponse;

@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;
}
