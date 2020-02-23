package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FavoriteResponseView {
    private Long id;
    private Long stationId;

    @Builder
    public FavoriteResponseView(Long stationId) {
        this.stationId = stationId;
    }

    public static FavoriteResponseView of(FavoriteStation savedFavorite) {
        return FavoriteResponseView.builder().stationId(savedFavorite.getStationId()).build();
    }
}
