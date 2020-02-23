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
    public FavoriteResponseView(Long id, Long stationId) {
        this.id = id;
        this.stationId = stationId;
    }

    public static FavoriteResponseView of(FavoriteStation savedFavorite) {
        return FavoriteResponseView.builder()
                .id(savedFavorite.getId())
                .stationId(savedFavorite.getStationId()).build();
    }
}
