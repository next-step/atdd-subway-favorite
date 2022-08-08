package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private FavoriteStationResponse source;
    private FavoriteStationResponse target;
}
