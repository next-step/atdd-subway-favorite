package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;


    public static FavoriteResponse of(Favorite favorite, Station source, Station target) {
        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public FavoriteResponse() {
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
