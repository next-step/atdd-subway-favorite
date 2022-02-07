package nextstep.path.application;

import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.line.domain.Line;
import nextstep.path.application.dto.PathRequest;
import nextstep.station.domain.Station;
import nextstep.line.domain.Sections;
import nextstep.line.domain.repository.LineRepository;
import nextstep.path.application.dto.PathResponse;
import nextstep.path.domain.PathFinder;
import nextstep.path.domain.dto.StationPaths;
import nextstep.station.application.StationService;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PathFacade {
    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathResponse findShortestPaths(PathRequest pathRequest) {
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());
        StationPaths stationPaths = allSections().shortestPaths(pathFinder, source, target);

        return PathResponse.from(stationPaths);
    }

    private Sections allSections() {
        return lineRepository.findAllWithStations()
                             .stream()
                             .map(Line::getSections)
                             .reduce(Sections::union)
                             .orElse(new Sections(Collections.emptyList()));
    }
}
