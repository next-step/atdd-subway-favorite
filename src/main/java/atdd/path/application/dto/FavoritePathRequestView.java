package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FavoritePathRequestView {
    private long sourceStationId;
    private long targetStationId;

    @Builder
    private FavoritePathRequestView(long sourceStationId, long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }
}
