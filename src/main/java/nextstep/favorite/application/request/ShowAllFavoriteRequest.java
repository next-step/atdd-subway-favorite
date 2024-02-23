package nextstep.favorite.application.request;

public class ShowAllFavoriteRequest {

    private Long startStationId;
    private Long endStationId;

    private ShowAllFavoriteRequest() {
    }

    private ShowAllFavoriteRequest(Long startStationId, Long endStationId) {
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }

    public static ShowAllFavoriteRequest of(Long startStationId, Long endStationId) {
        return new ShowAllFavoriteRequest(startStationId, endStationId);
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

}
