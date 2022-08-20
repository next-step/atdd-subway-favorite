package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

@Getter
public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = new StationResponse(source.getId(), source.getName());
        this.target = new StationResponse(target.getId(), target.getName());
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

}

