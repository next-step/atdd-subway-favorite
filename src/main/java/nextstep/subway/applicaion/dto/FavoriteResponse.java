package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(),StationResponse.of(favorite.getSource()),StationResponse.of(favorite.getTarget()));
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
