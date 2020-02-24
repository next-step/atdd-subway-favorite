package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoriteStation {
    private long id;
    private long owner;
    private long stationId;

    public FavoriteStation() {
    }

    @Builder
    private FavoriteStation(long id, long owner, long stationId) {
        this.id = id;
        this.owner = owner;
        this.stationId = stationId;
    }
}
