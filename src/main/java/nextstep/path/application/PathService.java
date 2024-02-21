package nextstep.path.application;

import nextstep.line.domain.Line;
import nextstep.line.domain.LineRepository;
import nextstep.path.application.dto.PathDto;
import nextstep.path.domain.Path;
import nextstep.path.domain.PathFinder;
import nextstep.station.domain.Station;
import nextstep.station.domain.StationRepository;
import nextstep.station.domain.exception.StationNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, PathFinder pathFinder) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public PathDto findShortestPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId).orElseThrow(StationNotFoundException::new);
        Station targetStation = stationRepository.findById(targetStationId).orElseThrow(StationNotFoundException::new);
        List<Line> lines = lineRepository.findAll();

        Path path = pathFinder.findShortestPathAndItsDistance(lines, sourceStation, targetStation);
        return PathDto.from(path);
    }
}
