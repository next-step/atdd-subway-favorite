package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.Station;

import java.util.List;

public interface ShortestPathService {

//    Path findShortestPath(Long sourceStationId, Long targetStationId);

    Path findShortestPath(List<Section> sections, Station sourceStation, Station targetStation);
}
