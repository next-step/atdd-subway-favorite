package nextstep.favorite.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

public class FavoriteResponse {
    @JsonProperty
    private Long id;
    @JsonProperty
    private StationResponse source;
    @JsonProperty
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Favorite favorite) {
        id = favorite.getId();
        source = new StationResponse(favorite.getSourceStation());
        target = new StationResponse(favorite.getTargetStation());
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
