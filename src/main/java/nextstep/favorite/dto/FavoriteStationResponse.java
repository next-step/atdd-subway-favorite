package nextstep.favorite.dto;

import nextstep.subway.station.domain.Station;

public class FavoriteStationResponse {
    private final Long id;
    private final String name;

    public static FavoriteStationResponse from(Station station) {
        return new FavoriteStationResponse(
                station.getId(),
                station.getName()
        );
    }

    public FavoriteStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
