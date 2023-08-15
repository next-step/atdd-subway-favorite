package nextstep.subway.application.dto.favorite;

import nextstep.subway.application.dto.station.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() { return id; }
    public StationResponse getSource() { return source; }
    public StationResponse getTarget() { return target; }
}