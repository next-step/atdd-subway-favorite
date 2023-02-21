package nextstep.favorite.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = new StationResponse(favorite.getSource());
        this.target = new StationResponse(favorite.getTarget());
    }
}
