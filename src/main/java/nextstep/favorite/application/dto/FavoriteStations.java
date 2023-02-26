package nextstep.favorite.application.dto;

import nextstep.subway.domain.Station;

public class FavoriteStations {
    private final Station source;
    private final Station target;

    public FavoriteStations(Station source, Station target) {
        this.source = source;
        this.target = target;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}