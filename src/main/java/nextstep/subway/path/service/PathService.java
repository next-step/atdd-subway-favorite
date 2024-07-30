package nextstep.subway.path.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;
import nextstep.subway.station.StationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathService {

    private final ShortestPathService shortestPathService;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final StationService stationService;

    public PathResponse findShortestPath(Long sourceStationId, Long targetStationId) {

        Station sourceStation = stationService.findById(sourceStationId);
        Station targetStation = stationService.findById(targetStationId);

        List<Section> sections = lineRepository.findAllWithSectionsAndStations()
                .stream().flatMap(line -> line.getSections().stream())
                .distinct()
                .collect(Collectors.toList());

        Path path = shortestPathService.findShortestPath(sections, sourceStation, targetStation);
        return PathResponse.from(path);
    }
}
