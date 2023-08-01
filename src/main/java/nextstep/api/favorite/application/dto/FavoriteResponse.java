package nextstep.api.favorite.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.api.favorite.domain.Favorite;
import nextstep.api.subway.applicaion.station.dto.StationResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse toResponse(final Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.toResponse(favorite.getSource()),
                StationResponse.toResponse(favorite.getTarget())
        );
    }
}
