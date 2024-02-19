package nextstep.subway.favorite.application.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.application.dto.StationResponseFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponseFactory {

    public static List<FavoriteResponse> create(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponseFactory::create)
                .collect(Collectors.toList());
    }

    public static FavoriteResponse create(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(),
                StationResponseFactory.create(favorite.getSourceStation()),
                StationResponseFactory.create(favorite.getTargetStation()));
    }
}
