package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteStationResponse {
    private long id;
    private long owner;
    private StationResponseView station;

    public FavoriteStationResponse() {
    }

    @Builder
    private FavoriteStationResponse(long id, long owner, StationResponseView station) {
        this.id = id;
        this.owner = owner;
        this.station = station;
    }
}
