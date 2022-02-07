package nextstep.path.domain;

import java.util.List;

import nextstep.line.domain.Section;
import nextstep.path.domain.dto.StationPaths;
import nextstep.station.domain.Station;

public interface PathFinder {
    StationPaths findShortestPaths(List<Section> sections, Station source, Station target);
}
