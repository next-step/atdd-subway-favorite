package nextstep.favorite.application.manager;

import nextstep.subway.domain.Station;

import java.util.Map;
import java.util.Set;

public interface FavoriteStationService {
    Map<Long, Station> loadFindStationsIds(Set<Long> stationIdx);
}
