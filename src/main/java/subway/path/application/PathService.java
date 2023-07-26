package subway.path.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.application.LineService;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathRetrieveResponse getShortestPath(long sourceStationId, long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<Line> lines = lineService.findByStation(sourceStation, targetStation);
        List<Section> sections = getAllSections(lines);
        return pathFinder.findShortestPath(sections, sourceStation, targetStation);
    }

    public PathRetrieveResponse getShortestPath(Station sourceStation, Station targetStation) {
        List<Line> lines = lineService.findByStation(sourceStation, targetStation);
        List<Section> sections = getAllSections(lines);
        return pathFinder.findShortestPath(sections, sourceStation, targetStation);
    }

    private List<Section> getAllSections(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getLineSections().getSections().stream())
                .collect(Collectors.toList());
    }


}
