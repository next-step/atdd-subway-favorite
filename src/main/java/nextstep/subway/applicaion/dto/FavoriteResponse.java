package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public FavoriteResponse() {
    }

    public static FavoriteResponse of(final Favorite favorite, final StationResponse source, final StationResponse target) {
        return new FavoriteResponse(favorite.getId(), source, target);
    }

    public Long getId() {
        return this.id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
