package nextstep.subway.favorite.dto;

import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse sourceResponse;
    private StationResponse targetResponse;

    public Long getId() {
        return id;
    }
}
