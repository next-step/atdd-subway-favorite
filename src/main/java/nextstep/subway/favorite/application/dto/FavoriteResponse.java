package nextstep.subway.favorite.application.dto;

import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Long id, Station source, Station target) {
        return new FavoriteResponse(id, StationResponse.from(source), StationResponse.from(target));
    }

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
