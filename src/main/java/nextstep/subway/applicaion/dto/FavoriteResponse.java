package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
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
