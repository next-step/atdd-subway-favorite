package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FavoriteRequest {

    @JsonProperty(value = "source")
    private final Long departureStationId;

    @JsonProperty(value = "target")
    private final Long destinationStationId;

    public FavoriteRequest(Long departureStationId, Long destinationStationId) {
        this.departureStationId = departureStationId;
        this.destinationStationId = destinationStationId;
    }

    public Long getDepartureStationId() {
        return departureStationId;
    }

    public Long getDestinationStationId() {
        return destinationStationId;
    }
}
