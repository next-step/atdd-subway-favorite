package nextstep.favorite.ui.dto;

import nextstep.station.application.dto.StationResponseBody;

public class FavoriteResponse {
    private Long id;
    private StationResponseBody source;
    private StationResponseBody target;

    public FavoriteResponse(Long id, StationResponseBody source, StationResponseBody target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponseBody getSource() {
        return source;
    }

    public StationResponseBody getTarget() {
        return target;
    }
}
