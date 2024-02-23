package nextstep.core.favorite.application.dto;

import nextstep.core.station.application.dto.StationResponse;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {

    private final StationResponse sourceStation;
    private final StationResponse targetStation;

    public FavoriteResponse(StationResponse sourceStation, StationResponse targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }
}
