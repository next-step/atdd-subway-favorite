package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class FavoritePath {
    @Setter
    private long id;
    @Setter
    private long owner;
    private long sourceStationId;
    private long targetStationId;

    @Setter
    private Station sourceStation;
    @Setter
    private Station targetStation;

    @Builder
    private FavoritePath(long id, long owner, long sourceStationId, long targetStationId) {
        this.id = id;
        this.owner = owner;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }


}
