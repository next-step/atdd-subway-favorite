package atdd.path.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FavoriteRouteRequestView {
    private Long id;
    private Long sourceStationId;
    private Long targetStationId;

    @Builder
    public FavoriteRouteRequestView(Long id, Long sourceStationId, Long targetStationId) {
        this.id = id;
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public void validate() {
        if (this.sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("Source station must not be equal to Target Station");
        }
    }
}
