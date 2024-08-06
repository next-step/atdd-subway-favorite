package nextstep.favorite.presentation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FavoriteRequest {
    private final Long sourceStationId;
    private final Long targetStationId;
}
