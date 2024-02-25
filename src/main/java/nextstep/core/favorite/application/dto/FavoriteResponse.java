package nextstep.core.favorite.application.dto;

import nextstep.core.station.application.dto.StationResponse;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {

    public StationResponse source;
    public StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(StationResponse source, StationResponse target) {
        this.source = source;
        this.target = target;
    }
}
