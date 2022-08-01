package nextstep.favorite.application.dto;

public class FavoriteResponse {

    private final Long id;
    private final FavoriteStationResponse source;
    private final FavoriteStationResponse target;

    private FavoriteResponse() {
        this.id = null;
        this.source = null;
        this.target = null;
    }

    public FavoriteResponse(final Long id, final FavoriteStationResponse source, final FavoriteStationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public FavoriteStationResponse getSource() {
        return source;
    }

    public FavoriteStationResponse getTarget() {
        return target;
    }
}
