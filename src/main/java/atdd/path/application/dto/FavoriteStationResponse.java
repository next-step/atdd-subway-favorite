package atdd.path.application.dto;

import atdd.path.domain.Station;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteStationResponse {
    private long id;
    private long owner;
    private Station station;

    public FavoriteStationResponse() {
    }

    @Builder
    private FavoriteStationResponse(long id, long owner, Station station) {
        this.id = id;
        this.owner = owner;
        this.station = station;
    }
}
