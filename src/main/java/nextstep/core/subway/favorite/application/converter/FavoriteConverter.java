package nextstep.core.subway.favorite.application.converter;

import nextstep.core.subway.favorite.application.dto.FavoriteResponse;
import nextstep.core.subway.favorite.domain.Favorite;
import nextstep.core.subway.station.application.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteConverter {

    public static List<FavoriteResponse> convertToResponses(List<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();

        favorites.forEach(favorite -> {
            favoriteResponses.add(new FavoriteResponse(
                    favorite.getId(),
                    new StationResponse(favorite.getSourceStation().getId(), favorite.getSourceStation().getName()),
                    new StationResponse(favorite.getTargetStation().getId(), favorite.getTargetStation().getName())));
        });
        return favoriteResponses;
    }
}
