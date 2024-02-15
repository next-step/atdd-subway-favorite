package nextstep.favorite.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.controller.dto.StationResponse;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static List<FavoriteResponse> listOf(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public static FavoriteResponse of(Favorite favorite) {
        Station source = favorite.sourceStation();
        Station target = favorite.targetStation();

        return FavoriteResponse.builder()
                .id(favorite.id())
                .source(new StationResponse(source.getId(), source.getName()))
                .target(new StationResponse(target.getId(), target.getName()))
                .build();
    }

}
