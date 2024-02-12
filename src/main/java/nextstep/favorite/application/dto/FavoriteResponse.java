package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.station.application.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(final Favorite saved) {
        return new FavoriteResponse(saved.getId(), StationResponse.from(saved.getSourceStation()), StationResponse.from(saved.getTargetStation()));
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
