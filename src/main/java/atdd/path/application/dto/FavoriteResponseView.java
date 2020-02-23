package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public static List<FavoriteResponseView> listOf(List<FavoriteStation> favorites) {
        return favorites.stream()
                .map(it -> FavoriteResponseView.builder()
                        .id(it.getId())
                        .stationId(it.getStationId())
                        .build())
                .collect(Collectors.toList());
    }
}
