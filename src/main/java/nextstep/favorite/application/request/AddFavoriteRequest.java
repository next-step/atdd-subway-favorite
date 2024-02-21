package nextstep.favorite.application.request;

public class AddFavoriteRequest {

    private Long startStationId;
    private Long endStationId;

    private AddFavoriteRequest() {
    }

    private AddFavoriteRequest(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public static AddFavoriteRequest of(Long startStationId, Long endStationId) {
        return new AddFavoriteRequest(startStationId, endStationId);
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

}
