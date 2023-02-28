package nextstep.subway.applicaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.subway.domain.Favorite;

@Getter
@AllArgsConstructor
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget()));
    }
}

