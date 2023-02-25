package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Favorite;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    private FavoriteResponse(final Long id, final StationResponse source, final StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(final Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSource()),
                StationResponse.of(favorite.getTarget())
        );
    }

    public static List<FavoriteResponse> of(final List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
