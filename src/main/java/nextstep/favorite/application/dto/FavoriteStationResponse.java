package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class FavoriteStationResponse {
    private Long id;
    private String name;
}
