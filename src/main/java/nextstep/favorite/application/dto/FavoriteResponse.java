package nextstep.favorite.application.dto;

import nextstep.subway.dto.station.StationResponse;
import nextstep.subway.entity.Station;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    protected FavoriteResponse() {}

    public FavoriteResponse(Long id, Station sourceStation, Station targetStation) {
        this.id = id;
        this.source = new StationResponse(sourceStation.getId(), sourceStation.getName());
        this.target = new StationResponse(targetStation.getId(), targetStation.getName());
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

