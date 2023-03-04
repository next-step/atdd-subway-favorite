package nextstep.subway.applicaion.dto;

public class FavoriteResponse {
    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    public FavoriteResponse(Long id, Long sourceId, String sourceName, Long targetId, String targetName) {
        this.id = id;
        this.source = new StationResponse(sourceId, sourceName);
        this.target = new StationResponse(targetId, targetName);
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
