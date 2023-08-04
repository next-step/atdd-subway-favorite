package nextstep.subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.entity.Favorite;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.from(favorite.getSource()),
                StationResponse.from(favorite.getTarget())
        );
    }
}
