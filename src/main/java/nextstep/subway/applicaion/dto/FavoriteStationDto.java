package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.time.LocalDateTime;

public class FavoriteStationDto {
    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public FavoriteStationDto(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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

    public static FavoriteStationDto of(Station station) {
        return new FavoriteStationDto(station.getId(), station.getName(), station.getCreatedDate(), station.getModifiedDate());
    }
}
