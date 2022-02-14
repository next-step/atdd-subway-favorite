package nextstep.member.application.dto;

import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse(StationResponse source, StationResponse target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget()));
    }

    public StationResponse getSource() {
        return source;
    }

    public void setSource(StationResponse source) {
        this.source = source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public void setTarget(StationResponse target) {
        this.target = target;
    }
}
