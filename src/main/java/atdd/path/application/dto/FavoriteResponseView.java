package atdd.path.application.dto;

import atdd.path.domain.FavoriteStation;
import atdd.path.domain.Station;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class FavoriteResponseView {
    private Long id;
    private Item station;

    @Builder
    public FavoriteResponseView(Long id, Station station) {
        this.id = id;
        this.station = Item.builder().id(station.getId()).name(station.getName()).build();
    }

    public static FavoriteResponseView of(FavoriteStation savedFavorite) {
        return FavoriteResponseView.builder()
                .id(savedFavorite.getId())
                .station(savedFavorite.getStation())
                .build();
    }

    public static List<FavoriteResponseView> listOf(List<FavoriteStation> favorites) {
        return favorites.stream()
                .map(it -> FavoriteResponseView.builder()
                        .id(it.getId())
                        .station(it.getStation())
                        .build())
                .collect(Collectors.toList());
    }
}
