package nextstep.favorite.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.Station;

public class FavoriteResponse {

    private Long id;

    @JsonProperty(value = "source")
    private StationResponse departureStationResponse;

    @JsonProperty(value = "target")
    private StationResponse destinationStationResponse;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, StationResponse departureStationResponse, StationResponse destinationStationResponse) {
        this.id = id;
        this.departureStationResponse = departureStationResponse;
        this.destinationStationResponse = destinationStationResponse;
    }

    public static FavoriteResponse of(Favorite favorite, Station departureStation, Station destinationStation) {
        return new FavoriteResponse(favorite.getId(), StationResponse.from(departureStation), StationResponse.from(destinationStation));
    }

    public Long getId() {
        return id;
    }

    public StationResponse getDepartureStationResponse() {
        return departureStationResponse;
    }

    public StationResponse getDestinationStationResponse() {
        return destinationStationResponse;
    }
}
