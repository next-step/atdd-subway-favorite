package atdd.path.application.dto;

import atdd.path.domain.FavoriteRoute;
import lombok.Builder;
import lombok.Getter;

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
}
