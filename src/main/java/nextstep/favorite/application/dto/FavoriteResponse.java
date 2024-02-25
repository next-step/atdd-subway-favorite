package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.domain.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

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
                StationResponse.from(favorite.getSourceStation()),
                StationResponse.from(favorite.getTargetStation())
        );
    }

    public static List<FavoriteResponse> listFrom(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
