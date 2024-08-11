package nextstep.favorite.application.dto;

import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse sourceStation;
    private StationResponse targetStation;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse sourceStation, StationResponse targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSourceStation() {
        return sourceStation;
    }

    public StationResponse getTargetStation() {
        return targetStation;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), new StationResponse(favorite.getSource()), new StationResponse(favorite.getTarget()));
    }
}
