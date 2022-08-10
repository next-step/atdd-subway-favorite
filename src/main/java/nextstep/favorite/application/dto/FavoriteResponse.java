package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse(Long id) {
        this.id = id;
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = favorite.getSource();
        this.target = favorite.getTarget();
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }

}
