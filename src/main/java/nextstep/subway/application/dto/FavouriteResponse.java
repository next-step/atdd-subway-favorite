package nextstep.subway.application.dto;

import nextstep.subway.application.dto.station.StationResponse;
import nextstep.subway.domain.favourite.Favourite;

public class FavouriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavouriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavouriteResponse of(Favourite favourite, StationResponse upStationResponse, StationResponse downStationResponse) {
        return new FavouriteResponse(favourite.getId(), upStationResponse, downStationResponse);
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
