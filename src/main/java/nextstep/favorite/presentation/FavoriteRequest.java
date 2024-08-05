package nextstep.favorite.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.favorite.domain.Favorite;

@RequiredArgsConstructor
@Getter
public class FavoriteRequest {
    private final Long sourceStationId;
    private final Long targetStationId;

    public Favorite toFavorite(Long memberId) {
        return new Favorite(memberId, sourceStationId, targetStationId);
    }
}
