package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.Station;

public class FavoriteResponse {

    private final Long id;
    private final FavoriteStationResponse source;
    private final FavoriteStationResponse target;

    public FavoriteResponse(Long id, FavoriteStationResponse source, FavoriteStationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite, Station source, Station target) {
        return new FavoriteResponse(
                favorite.getId(),
                FavoriteStationResponse.from(source),
                FavoriteStationResponse.from(target)
        );
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationResponse getSource() {
        return source;
    }

    public FavoriteStationResponse getTarget() {
        return target;
    }
}
