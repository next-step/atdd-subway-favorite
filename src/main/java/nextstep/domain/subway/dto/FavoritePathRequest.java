package nextstep.domain.subway.dto;


public class FavoritePathRequest {
    private Long startStationId;
    private Long endStationId;

    public FavoritePathRequest() {
    }

    public FavoritePathRequest(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }
}
