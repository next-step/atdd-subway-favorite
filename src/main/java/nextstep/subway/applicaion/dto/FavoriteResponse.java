package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id) {
        this(id, null, null);
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId());
    }

    public static FavoriteResponse of(Favorite favorite, Station source, Station target) {
        return new FavoriteResponse(favorite.getId(), StationResponse.from(source), StationResponse.from(target));
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
