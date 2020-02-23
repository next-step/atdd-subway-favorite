package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FavoriteStation {
    private Long id;
    private Long userId;
    private Long stationId;

    @Builder
    public FavoriteStation(Long userId, Long stationId) {
        this.userId = userId;
        this.stationId = stationId;
    }
}
