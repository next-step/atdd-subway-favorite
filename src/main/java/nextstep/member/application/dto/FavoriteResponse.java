package nextstep.member.application.dto;

import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() { }

    private FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public static FavoriteResponse of(Long id, Station source, Station target) {
        return new FavoriteResponse(id, source, target);
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
