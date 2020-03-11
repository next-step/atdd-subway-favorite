package atdd.path.application.dto;

import atdd.path.domain.FavoriteRoute;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class FavoriteRouteResponseView {
    private Long id;
    private Item sourceStation;
    private Item targetStation;

    public static FavoriteRouteResponseView of(FavoriteRoute favorite) {
        return FavoriteRouteResponseView.builder()
                .id(favorite.getId())
                .sourceStation(Item.builder()
                        .id(favorite.getSourceStation().getId())
                        .name(favorite.getSourceStation().getName())
                        .build())
                .targetStation(Item.builder()
                        .id(favorite.getTargetStation().getId())
                        .name(favorite.getTargetStation().getName())
                        .build())
                .build();
    }

    public static List<FavoriteRouteResponseView> listOf(List<FavoriteRoute> favorites) {
        return favorites.stream()
                .map(it -> FavoriteRouteResponseView.builder()
                        .id(it.getId())
                        .sourceStation(Item.builder()
                                .id(it.getSourceStation().getId())
                                .name(it.getSourceStation().getName())
                                .build())
                        .targetStation(Item.builder()
                                .id(it.getTargetStation().getId())
                                .name(it.getTargetStation().getName())
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
