package nextstep.favorite.application.dto;

import nextstep.subway.domain.Station;

import java.time.LocalDateTime;

public class FavoriteStationResponse {

    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    private FavoriteStationResponse() {
        this.id = null;
        this.name = null;
        this.createdDate = null;
        this.modifiedDate = null;
    }

    public FavoriteStationResponse(final Station station) {
        this.id = station.getId();
        this.name = station.getName();
        this.createdDate = station.getCreatedDate();
        this.modifiedDate = station.getModifiedDate();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
}
