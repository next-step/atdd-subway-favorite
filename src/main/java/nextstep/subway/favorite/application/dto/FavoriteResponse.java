package nextstep.subway.favorite.application.dto;

import nextstep.subway.station.StationResponse;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
