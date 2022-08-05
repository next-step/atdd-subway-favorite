package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Favorite favorite, Station source, Station target) {
        this.id = favorite.getId();
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
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
