package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationResponse;

/**
 * TODO: StationResponse를 포함하는 클래스로 만듭니다.
 */
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = new StationResponse(source);
        this.target = new StationResponse(target);
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = new StationResponse(favorite.getSourceStation());
        this.target = new StationResponse(favorite.getTargetStation());
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
