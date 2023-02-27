package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public static FavoriteResponse of(Favorite favorite, Station source, Station target) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
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
