package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;
}
