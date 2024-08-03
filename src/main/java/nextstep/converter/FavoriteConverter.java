package nextstep.converter;

import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

import static nextstep.converter.StationConverter.stationToStationResponse;

public class FavoriteConverter {

    private FavoriteConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<FavoriteResponse> favoriteListToFavoriteResponseList(final List<Favorite> favoriteList) {
        List<FavoriteResponse> favoriteResponseList = new ArrayList<>();

        for (Favorite favorite : favoriteList) {
            StationResponse sourceStationResponse = stationToStationResponse(favorite.getSourceStation());
            StationResponse targetStationResponse = stationToStationResponse(favorite.getTargetStation());;
            FavoriteResponse favoriteResponse = FavoriteResponse.of(favorite.getId(), sourceStationResponse, targetStationResponse);
            favoriteResponseList.add(favoriteResponse);
        }

        return favoriteResponseList;
    }
}
