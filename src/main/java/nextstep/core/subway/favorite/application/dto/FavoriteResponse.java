package nextstep.core.subway.favorite.application.dto;

import nextstep.core.subway.station.application.dto.StationResponse;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {

    public Long id;
    public StationResponse source;
    public StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }
}
