package nextstep.util;

import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class ResponseMapper {
    private ResponseMapper() {
    }

    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), from(favorite.getSource()), from(favorite.getTarget()));
    }

    private static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<FavoriteResponse> from(List<Favorite> favorites) {
        return favorites.stream().map(ResponseMapper::from)
                .collect(Collectors.toList());
    }
}
