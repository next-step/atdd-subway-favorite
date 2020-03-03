package atdd.path.application.dto;

import lombok.Getter;

@Getter
public class FavoriteRouteResponseView {
    private Long id;
    private Item sourceStation;
    private Item targetStation;
}
