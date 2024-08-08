package nextstep.station.application.dto;

import nextstep.station.domain.Station;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static StationResponse createResponse(Station line) {
        return new StationResponse(
                line.getId(),
                line.getName()
        );
    }
}
