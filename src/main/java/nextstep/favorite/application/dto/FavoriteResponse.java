package nextstep.favorite.application.dto;

import nextstep.station.Station;
import nextstep.station.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = new StationResponse(source);
        this.target = new StationResponse(target);
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
