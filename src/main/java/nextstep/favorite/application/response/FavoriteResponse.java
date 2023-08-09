package nextstep.favorite.application.response;

import nextstep.favorite.domain.Favorite;
import nextstep.station.application.response.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setSource(StationResponse source) {
        this.source = source;
    }

    public void setTarget(StationResponse target) {
        this.target = target;
    }
}
