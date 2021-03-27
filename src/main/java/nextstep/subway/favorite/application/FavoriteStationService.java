package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FavoriteStationService {

    private final StationService stationService;

    public FavoriteStationService(StationService stationService) {
        this.stationService = stationService;
    }

    public FavoriteResponse convertToFavoriteResponse(Favorite favorite) {
        Station source = stationService.findStationById(favorite.getSourceId());
        Station target = stationService.findStationById(favorite.getTargetId());

        return new FavoriteResponse(favorite.getId(), StationResponse.of(source), StationResponse.of(target));
    }

    public List<FavoriteResponse> convertToFavoriteResponses(List<Favorite> favorites) {
        return favorites.stream()
                .map(this::convertToFavoriteResponse)
                .collect(Collectors.toList());
    }
}
