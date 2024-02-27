package nextstep.favorite.ui.dto;

import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.station.application.dto.StationResponseBody;

public class FavoriteResponse {
    private Long id;
    private StationResponseBody source;
    private StationResponseBody target;

    public FavoriteResponse(Long id, StationResponseBody source, StationResponseBody target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponseBody getSource() {
        return source;
    }

    public StationResponseBody getTarget() {
        return target;
    }

    public static FavoriteResponse from(FavoriteDto favoriteDto) {
        StationResponseBody source = StationResponseBody.from(favoriteDto.getSource());
        StationResponseBody target = StationResponseBody.from(favoriteDto.getTarget());
        return new FavoriteResponse(favoriteDto.getId(), source, target);
    }
}
