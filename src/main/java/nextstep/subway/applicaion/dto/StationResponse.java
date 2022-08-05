package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;
    private LocalDateTime modifiedDate;
    private LocalDateTime createdDate;


    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName(), station.getModifiedDate(), station.getCreatedDate());
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse() {
    }

    public StationResponse(final Long id, final String name, final LocalDateTime modifiedDate, final LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
