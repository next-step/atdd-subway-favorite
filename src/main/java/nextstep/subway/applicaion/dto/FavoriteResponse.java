package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FavoriteResponse {

    private final Long id;
    private final StationResponse source;
    private final StationResponse target;

    private FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static List<FavoriteResponse> fromList(final List<Favorite> favoriteList) {
        return favoriteList.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    public static FavoriteResponse from(final Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSourceStation()),
                StationResponse.of(favorite.getTargetStation())
        );
    }
}
