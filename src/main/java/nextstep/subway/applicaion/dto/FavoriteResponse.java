package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;
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
        return new FavoriteResponse(favorite.getId(),
            StationResponse.of(favorite.getSource()),
            StationResponse.of(favorite.getTarget()));
    }

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }
}
