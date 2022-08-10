package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.favorite.domain.Favorite;

@Getter
public class FavoriteResponse {
    private Long id;
    private FavoriteStationResponse source;
    private FavoriteStationResponse target;

    public FavoriteResponse(Long id, FavoriteStationResponse source,
        FavoriteStationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(),
            FavoriteStationResponse.of(favorite.getSource()),
            FavoriteStationResponse.of(favorite.getTarget()));
    }

}
