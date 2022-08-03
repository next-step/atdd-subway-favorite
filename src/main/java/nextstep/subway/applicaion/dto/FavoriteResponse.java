package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Favorite;

@Getter
@RequiredArgsConstructor
public class FavoriteResponse {
    private final Long id;
    private final FavoriteStationDto source;
    private final FavoriteStationDto target;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), FavoriteStationDto.of(favorite.getSource()), FavoriteStationDto.of(favorite.getTarget()));
    }

}
