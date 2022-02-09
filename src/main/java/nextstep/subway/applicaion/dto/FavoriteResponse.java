package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse() {
    }

    private FavoriteResponse(long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(long id, Station source, Station target) {
        StationResponse sourceResponse = StationResponse.of(source);
        StationResponse targetResponse = StationResponse.of(target);
        return new FavoriteResponse(id, sourceResponse, targetResponse);
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
