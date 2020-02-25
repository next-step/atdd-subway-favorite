package atdd.path.application.dto.favorite;

import atdd.path.domain.Favorite;
import atdd.path.domain.Station;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class FavoriteCreateRequestView {
    private Long stationId;
    private String stationName;

    @Builder
    public FavoriteCreateRequestView(Long stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public Favorite toEntity() {
        return Favorite.builder()
                .station(new Station(stationId, stationName))
                .build();
    }
}
