package nextstep.subway.applicaion.dto;

public class FavoriteResponse {

    private final long id;
    private final StationResponse source;
    private final StationResponse target;


    public FavoriteResponse(final long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

}
