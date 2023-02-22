package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse toResponse(Favorite favorite, Station upStation, Station downStation) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(upStation), StationResponse.of(downStation));
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
