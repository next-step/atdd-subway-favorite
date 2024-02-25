package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse ofEntity(Long id, Station sourceStation, Station targetStation) {
        return new FavoriteResponse(
                id,
                StationResponse.ofEntity(sourceStation),
                StationResponse.ofEntity(targetStation)
        );
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
