package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        Station source = favorite.getSource();
        this.source = new StationResponse(source.getId(), source.getName());

        Station target = favorite.getTarget();
        this.target = new StationResponse(target.getId(), target.getName());
    }
}
