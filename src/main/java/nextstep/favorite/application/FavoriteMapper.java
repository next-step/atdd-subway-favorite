package nextstep.favorite.application;

import lombok.RequiredArgsConstructor;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.subway.station.application.StationReader;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteMapper {
  private final StationReader stationReader;

  public FavoriteResponse mapToFavoriteResponse(Favorite favorite) {
    Station source = stationReader.readById(favorite.getSourceStationId());
    Station target = stationReader.readById(favorite.getTargetStationId());
    return FavoriteResponse.of(favorite.getId(), source, target);
  }
}
