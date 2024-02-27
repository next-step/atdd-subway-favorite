package nextstep.favorite.application.dto;

import nextstep.subway.station.controller.dto.StationResponse;
import nextstep.subway.station.domain.Station;

/** 즐겨찾기 조회 응답 정보 */
public class FavoriteResponse {

    private long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(long id, Station source, Station target) {
        return new FavoriteResponse(
            id,
            StationResponse.of(source),
            StationResponse.of(target)
        );
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
